/*
 * This file is part of BuildDynasty.
 *
 * BuildDynasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BuildDynasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BuildDynasty.  If not, see <https://www.gnu.org/licenses/>.
 */

package BuildDynasty.launch.mixins;

import BuildDynasty.api.BuildDynastyAPI;
import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.event.events.RotationMoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static org.spongepowered.asm.lib.Opcodes.GETFIELD;

/**
 * @author XINBOSHIN
 * @since 9/10/2018
 */
@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    /**
     * Event called to override the movement direction when jumping
     */
    @Unique
    private RotationMoveEvent jumpRotationEvent;

    @Unique
    private RotationMoveEvent elytraRotationEvent;

    private MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "jump",
            at = @At("HEAD")
    )
    private void preMoveRelative(CallbackInfo ci) {
        this.getBuildDynasty().ifPresent(BuildDynasty -> {
            this.jumpRotationEvent = new RotationMoveEvent(RotationMoveEvent.Type.JUMP, this.rotationYaw, this.rotationPitch);
            BuildDynasty.getGameEventHandler().onPlayerRotationMove(this.jumpRotationEvent);
        });
    }

    @Redirect(
            method = "jump",
            at = @At(
                    value = "FIELD",
                    opcode = GETFIELD,
                    target = "net/minecraft/entity/EntityLivingBase.rotationYaw:F"
            )
    )
    private float overrideYaw(EntityLivingBase self) {
        if (self instanceof EntityPlayerSP && BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this) != null) {
            return this.jumpRotationEvent.getYaw();
        }
        return self.rotationYaw;
    }

    @Inject(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/entity/EntityLivingBase.getLookVec()Lnet/minecraft/util/math/Vec3d;"
            )
    )
    private void onPreElytraMove(float strafe, float vertical, float forward, CallbackInfo ci) {
        this.getBuildDynasty().ifPresent(BuildDynasty -> {
            this.elytraRotationEvent = new RotationMoveEvent(RotationMoveEvent.Type.MOTION_UPDATE, this.rotationYaw, this.rotationPitch);
            BuildDynasty.getGameEventHandler().onPlayerRotationMove(this.elytraRotationEvent);
            this.rotationYaw = this.elytraRotationEvent.getYaw();
            this.rotationPitch = this.elytraRotationEvent.getPitch();
        });
    }

    @Inject(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/entity/EntityLivingBase.move(Lnet/minecraft/entity/MoverType;DDD)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onPostElytraMove(float strafe, float vertical, float forward, CallbackInfo ci) {
        if (this.elytraRotationEvent != null) {
            this.rotationYaw = this.elytraRotationEvent.getOriginal().getYaw();
            this.rotationPitch = this.elytraRotationEvent.getOriginal().getPitch();
            this.elytraRotationEvent = null;
        }
    }

    @Redirect(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/entity/EntityLivingBase.moveRelative(FFFF)V"
            )
    )
    private void onMoveRelative(EntityLivingBase self, float strafe, float up, float forward, float friction) {
        Optional<IBuildDynasty> BuildDynasty = this.getBuildDynasty();
        if (!BuildDynasty.isPresent()) {
            // If a shadow is used here it breaks on Forge
            this.moveRelative(strafe, up, forward, friction);
            return;
        }

        RotationMoveEvent event = new RotationMoveEvent(RotationMoveEvent.Type.MOTION_UPDATE, this.rotationYaw, this.rotationPitch);
        BuildDynasty.get().getGameEventHandler().onPlayerRotationMove(event);

        this.rotationYaw = event.getYaw();
        this.rotationPitch = event.getPitch();

        this.moveRelative(strafe, up, forward, friction);

        this.rotationYaw = event.getOriginal().getYaw();
        this.rotationPitch = event.getOriginal().getPitch();
    }

    @Unique
    private Optional<IBuildDynasty> getBuildDynasty() {
        // noinspection ConstantConditions
        if (EntityPlayerSP.class.isInstance(this)) {
            return Optional.ofNullable(BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this));
        } else {
            return Optional.empty();
        }
    }
}
