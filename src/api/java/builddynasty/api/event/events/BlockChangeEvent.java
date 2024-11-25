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

package BuildDynasty.api.event.events;

import BuildDynasty.api.utils.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

/**
 * @author XINBOSHIN
 */
public final class BlockChangeEvent {

    private final ChunkPos chunk;
    private final List<Pair<BlockPos, IBlockState>> blocks;

    public BlockChangeEvent(ChunkPos pos, List<Pair<BlockPos, IBlockState>> blocks) {
        this.chunk = pos;
        this.blocks = blocks;
    }

    public ChunkPos getChunkPos() {
        return this.chunk;
    }

    public List<Pair<BlockPos, IBlockState>> getBlocks() {
        return this.blocks;
    }
}
