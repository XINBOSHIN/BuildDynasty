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

package BuildDynasty.utils.player;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.cache.IWorldData;
import BuildDynasty.api.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of {@link IPlayerContext} that provides information about the primary player.
 *
 * @author XINBOSHIN
 * @since 11/12/2018
 */
public final class BuildDynastyPlayerContext implements IPlayerContext {

    private final BuildDynasty BuildDynasty;
    private final Minecraft mc;
    private final IPlayerController playerController;

    public BuildDynastyPlayerContext(BuildDynasty BuildDynasty, Minecraft mc) {
        this.BuildDynasty = BuildDynasty;
        this.mc = mc;
        this.playerController = new BuildDynastyPlayerController(mc);
    }

    @Override
    public Minecraft minecraft() {
        return this.mc;
    }

    @Override
    public EntityPlayerSP player() {
        return this.mc.player;
    }

    @Override
    public IPlayerController playerController() {
        return this.playerController;
    }

    @Override
    public World world() {
        return this.mc.world;
    }

    @Override
    public IWorldData worldData() {
        return this.BuildDynasty.getWorldProvider().getCurrentWorld();
    }

    @Override
    public BetterBlockPos viewerPos() {
        final Entity entity = this.mc.getRenderViewEntity();
        return entity == null ? this.playerFeet() : BetterBlockPos.from(new BlockPos(entity));
    }

    @Override
    public Rotation playerRotations() {
        return this.BuildDynasty.getLookBehavior().getEffectiveRotation().orElseGet(IPlayerContext.super::playerRotations);
    }

    @Override
    public RayTraceResult objectMouseOver() {
        return RayTraceUtils.rayTraceTowards(player(), playerRotations(), playerController().getBlockReachDistance());
    }
}
