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

package BuildDynasty.utils.schematic;

import BuildDynasty.api.schematic.ISchematic;
import BuildDynasty.api.schematic.MaskSchematic;
import BuildDynasty.api.selection.ISelection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

import java.util.stream.Stream;

public class SelectionSchematic extends MaskSchematic {

    private final ISelection[] selections;

    public SelectionSchematic(ISchematic schematic, Vec3i origin, ISelection[] selections) {
        super(schematic);
        this.selections = Stream.of(selections).map(
                        sel -> sel
                                .shift(EnumFacing.WEST, origin.getX())
                                .shift(EnumFacing.DOWN, origin.getY())
                                .shift(EnumFacing.NORTH, origin.getZ()))
                .toArray(ISelection[]::new);
    }

    @Override
    protected boolean partOfMask(int x, int y, int z, IBlockState currentState) {
        for (ISelection selection : selections) {
            if (x >= selection.min().x && y >= selection.min().y && z >= selection.min().z
                    && x <= selection.max().x && y <= selection.max().y && z <= selection.max().z) {
                return true;
            }
        }
        return false;
    }
}
