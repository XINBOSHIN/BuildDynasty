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

package BuildDynasty.api.pathing.movement;

import BuildDynasty.api.utils.BetterBlockPos;
import net.minecraft.util.math.BlockPos;

/**
 * @author XINBOSHIN
 * @since 10/8/2018
 */
public interface IMovement {

    double getCost();

    MovementStatus update();

    /**
     * Resets the current state status to {@link MovementStatus#PREPPING}
     */
    void reset();

    /**
     * Resets the cache for special break, place, and walk into blocks
     */
    void resetBlockCache();

    /**
     * @return Whether or not it is safe to cancel the current movement state
     */
    boolean safeToCancel();

    boolean calculatedWhileLoaded();

    BetterBlockPos getSrc();

    BetterBlockPos getDest();

    BlockPos getDirection();
}
