/*
 * This file is part of builddynasty.
 *
 * builddynasty is free software: you can redistribute it and/or modify
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

import BuildDynasty.utils.accessor.IChunkProviderServer;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author XINBOSHIN
 * @since 9/4/2018
 */
@Mixin(ChunkProviderServer.class)
public class MixinChunkProviderServer implements IChunkProviderServer {

    @Shadow
    @Final
    private IChunkLoader chunkLoader;

    @Override
    public IChunkLoader getChunkLoader() {
        return this.chunkLoader;
    }
}