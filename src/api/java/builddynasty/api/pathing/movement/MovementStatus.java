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

/**
 * @author XINBOSHIN
 * @since 10/8/2018
 */
public enum MovementStatus {

    /**
     * We are preparing the movement to be executed. This is when any blocks obstructing the destination are broken.
     */
    PREPPING(false),

    /**
     * We are waiting for the movement to begin, after {@link MovementStatus#PREPPING}.
     */
    WAITING(false),

    /**
     * The movement is currently in progress, after {@link MovementStatus#WAITING}
     */
    RUNNING(false),

    /**
     * The movement has been completed and we are at our destination
     */
    SUCCESS(true),

    /**
     * There was a change in state between calculation and actual
     * movement execution, and the movement has now become impossible.
     */
    UNREACHABLE(true),

    /**
     * Unused
     */
    FAILED(true),

    /**
     * "Unused"
     */
    CANCELED(true);

    /**
     * Whether or not this status indicates a complete movement.
     */
    private final boolean complete;

    MovementStatus(boolean complete) {
        this.complete = complete;
    }

    public final boolean isComplete() {
        return this.complete;
    }
}
