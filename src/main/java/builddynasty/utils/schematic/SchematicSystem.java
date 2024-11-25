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

import BuildDynasty.api.command.registry.Registry;
import BuildDynasty.api.schematic.ISchematicSystem;
import BuildDynasty.api.schematic.format.ISchematicFormat;
import BuildDynasty.utils.schematic.format.DefaultSchematicFormats;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author XINBOSHIN
 * @since 12/24/2019
 */
public enum SchematicSystem implements ISchematicSystem {
    INSTANCE;

    private final Registry<ISchematicFormat> registry = new Registry<>();

    SchematicSystem() {
        Arrays.stream(DefaultSchematicFormats.values()).forEach(this.registry::register);
    }

    @Override
    public Registry<ISchematicFormat> getRegistry() {
        return this.registry;
    }

    @Override
    public Optional<ISchematicFormat> getByFile(File file) {
        return this.registry.stream().filter(format -> format.isFileType(file)).findFirst();
    }
}
