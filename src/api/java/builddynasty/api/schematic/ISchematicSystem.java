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

import BuildDynasty.api.command.registry.Registry;
import BuildDynasty.api.schematic.format.ISchematicFormat;

import java.io.File;
import java.util.Optional;

/**
 * @author XINBOSHIN
 * @since 12/23/2019
 */
public interface ISchematicSystem {

    /**
     * @return The registry of supported schematic formats
     */
    Registry<ISchematicFormat> getRegistry();

    /**
     * Attempts to find an {@link ISchematicFormat} that supports the specified schematic file.
     *
     * @param file A schematic file
     * @return The corresponding format for the file, {@link Optional#empty()} if no candidates were found.
     */
    Optional<ISchematicFormat> getByFile(File file);
}
