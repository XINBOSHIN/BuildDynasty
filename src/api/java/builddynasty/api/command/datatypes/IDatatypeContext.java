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

package BuildDynasty.api.command.datatypes;

import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.command.argument.IArgConsumer;

/**
 * Provides an {@link IDatatype} with contextual information so
 * that it can perform the desired operation on the target level.
 *
 * @author XINBOSHIN
 * @see IDatatype
 * @since 9/26/2019
 */
public interface IDatatypeContext {

    /**
     * Provides the {@link IBuildDynasty} instance that is associated with the action relating to datatype handling.
     *
     * @return The context {@link IBuildDynasty} instance.
     */
    IBuildDynasty getBuildDynasty();

    /**
     * Provides the {@link IArgConsumer}} to fetch input information from.
     *
     * @return The context {@link IArgConsumer}}.
     */
    IArgConsumer getConsumer();
}
