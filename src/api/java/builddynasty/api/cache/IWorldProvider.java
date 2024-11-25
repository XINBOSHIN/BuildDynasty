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

package BuildDynasty.api.cache;

import java.util.function.Consumer;

/**
 * @author XINBOSHIN
 * @since 9/24/2018
 */
public interface IWorldProvider {

    /**
     * Returns the data of the currently loaded world
     *
     * @return The current world data
     */
    IWorldData getCurrentWorld();

    default void ifWorldLoaded(Consumer<IWorldData> callback) {
        final IWorldData currentWorld = this.getCurrentWorld();
        if (currentWorld != null) {
            callback.accept(currentWorld);
        }
    }
}
