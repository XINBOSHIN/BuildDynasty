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

package BuildDynasty.api.schematic;

import BuildDynasty.api.utils.BlockOptionalMeta;
import net.minecraft.block.state.IBlockState;

import java.util.List;

public class FillSchematic extends AbstractSchematic {

    private final BlockOptionalMeta bom;

    public FillSchematic(int x, int y, int z, BlockOptionalMeta bom) {
        super(x, y, z);
        this.bom = bom;
    }

    public FillSchematic(int x, int y, int z, IBlockState state) {
        this(x, y, z, new BlockOptionalMeta(state.getBlock(), state.getBlock().getMetaFromState(state)));
    }

    public BlockOptionalMeta getBom() {
        return bom;
    }

    @Override
    public IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable) {
        if (bom.matches(current)) {
            return current;
        }
        for (IBlockState placeable : approxPlaceable) {
            if (bom.matches(placeable)) {
                return placeable;
            }
        }
        return bom.getAnyBlockState();
    }
}
