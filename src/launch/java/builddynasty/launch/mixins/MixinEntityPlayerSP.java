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
import BuildDynasty.api.event.events.ChatEvent;
import BuildDynasty.api.event.events.PlayerUpdateEvent;
import BuildDynasty.api.event.events.SprintStateEvent;
import BuildDynasty.api.event.events.type.EventState;
import BuildDynasty.behavior.LookBehavior;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author XINBOSHIN
 * @since 8/1/2018
 */
@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(
            method = "sendChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sendChatMessage(String msg, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(msg);
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this);
        if (BuildDynasty == null) {
            return;
        }
        BuildDynasty.getGameEventHandler().onSendChatMessage(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/entity/AbstractClientPlayer.onUpdate()V",
                    shift = At.Shift.AFTER
            )
    )
    private void onPreUpdate(CallbackInfo ci) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this);
        if (BuildDynasty != null) {
            BuildDynasty.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.PRE));
        }
    }

    @Redirect(
            method = "onLivingUpdate",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/entity/player/PlayerCapabilities.allowFlying:Z"
            )
    )
    private boolean isAllowFlying(PlayerCapabilities capabilities) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this);
        if (BuildDynasty == null) {
            return capabilities.allowFlying;
        }
        return !BuildDynasty.getPathingBehavior().isPathing() && capabilities.allowFlying;
    }

    @Redirect(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/settings/KeyBinding.isKeyDown()Z"
            )
    )
    private boolean isKeyDown(KeyBinding keyBinding) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this);
        if (BuildDynasty == null) {
            return keyBinding.isKeyDown();
        }
        SprintStateEvent event = new SprintStateEvent();
        BuildDynasty.getGameEventHandler().onPlayerSprintState(event);
        if (event.getState() != null) {
            return event.getState();
        }
        if (BuildDynasty != BuildDynastyAPI.getProvider().getPrimaryBuildDynasty()) {
            // hitting control shouldn't make all bots sprint
            return false;
        }
        return keyBinding.isKeyDown();
    }

    @Inject(
            method = "updateRidden",
            at = @At(
                    value = "HEAD"
            )
    )
    private void updateRidden(CallbackInfo cb) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this);
        if (BuildDynasty != null) {
            ((LookBehavior) BuildDynasty.getLookBehavior()).pig();
        }
    }

    @Redirect(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/item/ItemElytra.isUsable(Lnet/minecraft/item/ItemStack;)Z"
            )
    )
    private boolean isElytraUsable(ItemStack stack) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForPlayer((EntityPlayerSP) (Object) this);
        if (BuildDynasty != null && BuildDynasty.getPathingBehavior().isPathing()) {
            return false;
        }
        return ItemElytra.isUsable(stack);
    }
}
