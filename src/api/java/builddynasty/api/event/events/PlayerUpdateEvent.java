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

import BuildDynasty.api.event.events.type.EventState;

/**
 * @author XINBOSHIN
 * @since 8/21/2018
 */
public final class PlayerUpdateEvent {

    /**
     * The state of the event
     */
    private final EventState state;

    public PlayerUpdateEvent(EventState state) {
        this.state = state;
    }

    /**
     * @return The state of the event
     */
    public final EventState getState() {
        return this.state;
    }
}
