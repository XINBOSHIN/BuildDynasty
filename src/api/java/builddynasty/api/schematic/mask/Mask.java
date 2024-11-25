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

package BuildDynasty.api.schematic.mask;

import BuildDynasty.api.schematic.mask.operator.BinaryOperatorMask;
import BuildDynasty.api.schematic.mask.operator.NotMask;
import BuildDynasty.api.utils.BooleanBinaryOperators;
import net.minecraft.block.state.IBlockState;

/**
 * @author XINBOSHIN
 */
public interface Mask {

    /**
     * @param x            The relative x position of the block
     * @param y            The relative y position of the block
     * @param z            The relative z position of the block
     * @param currentState The current state of that block in the world, may be {@code null}
     * @return Whether the given position is included in this mask
     */
    boolean partOfMask(int x, int y, int z, IBlockState currentState);

    int widthX();

    int heightY();

    int lengthZ();

    default Mask not() {
        return new NotMask(this);
    }

    default Mask union(Mask other) {
        return new BinaryOperatorMask(this, other, BooleanBinaryOperators.OR);
    }

    default Mask intersection(Mask other) {
        return new BinaryOperatorMask(this, other, BooleanBinaryOperators.AND);
    }

    default Mask xor(Mask other) {
        return new BinaryOperatorMask(this, other, BooleanBinaryOperators.XOR);
    }
}
