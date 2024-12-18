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

package BuildDynasty.api.utils;

import BuildDynasty.api.utils.accessor.IItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BlockOptionalMetaLookup {
    private final ImmutableSet<Block> blockSet;
    private final ImmutableSet<IBlockState> blockStateSet;
    private final ImmutableSet<Integer> stackHashes;
    private final BlockOptionalMeta[] boms;

    public BlockOptionalMetaLookup(BlockOptionalMeta... boms) {
        this.boms = boms;
        Set<Block> blocks = new HashSet<>();
        Set<IBlockState> blockStates = new HashSet<>();
        Set<Integer> stacks = new HashSet<>();
        for (BlockOptionalMeta bom : boms) {
            blocks.add(bom.getBlock());
            blockStates.addAll(bom.getAllBlockStates());
            stacks.addAll(bom.stackHashes());
        }
        this.blockSet = ImmutableSet.copyOf(blocks);
        this.blockStateSet = ImmutableSet.copyOf(blockStates);
        this.stackHashes = ImmutableSet.copyOf(stacks);
    }

    public BlockOptionalMetaLookup(Block... blocks) {
        this(Stream.of(blocks)
                .map(BlockOptionalMeta::new)
                .toArray(BlockOptionalMeta[]::new));

    }

    public BlockOptionalMetaLookup(List<Block> blocks) {
        this(blocks.stream()
                .map(BlockOptionalMeta::new)
                .toArray(BlockOptionalMeta[]::new));
    }

    public BlockOptionalMetaLookup(String... blocks) {
        this(Stream.of(blocks)
                .map(BlockOptionalMeta::new)
                .toArray(BlockOptionalMeta[]::new));
    }

    public boolean has(Block block) {
        return blockSet.contains(block);
    }

    public boolean has(IBlockState state) {
        return blockStateSet.contains(state);
    }

    public boolean has(ItemStack stack) {
        int hash = ((IItemStack) (Object) stack).getBuildDynastyHash();
        return stackHashes.contains(hash)
                || stackHashes.contains(hash - stack.getItemDamage());
    }

    public List<BlockOptionalMeta> blocks() {
        return Arrays.asList(boms);
    }

    @Override
    public String toString() {
        return String.format(
                "BlockOptionalMetaLookup{%s}",
                Arrays.toString(boms)
        );
    }
}
