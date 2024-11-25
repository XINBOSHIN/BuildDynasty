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

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.BuildDynastyAPI;
import BuildDynasty.api.utils.IPlayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkRenderWorker.class)
public abstract class MixinChunkRenderWorker {

    @Shadow
    protected abstract boolean isChunkExisting(BlockPos pos, World worldIn);

    @Redirect(
            method = "processTask",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/renderer/chunk/ChunkRenderWorker.isChunkExisting(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;)Z"
            )
    )
    private boolean isChunkExisting(ChunkRenderWorker worker, BlockPos pos, World world) {
        if (BuildDynasty.settings().renderCachedChunks.value && !Minecraft.getMinecraft().isSingleplayer()) {
            BuildDynasty BuildDynasty = (BuildDynasty) BuildDynastyAPI.getProvider().getPrimaryBuildDynasty();
            IPlayerContext ctx = BuildDynasty.getPlayerContext();
            if (ctx.player() != null && ctx.world() != null && BuildDynasty.bsi != null) {
                return BuildDynasty.bsi.isLoaded(pos.getX(), pos.getZ()) || this.isChunkExisting(pos, world);
            }
        }

        return this.isChunkExisting(pos, world);
    }
}

