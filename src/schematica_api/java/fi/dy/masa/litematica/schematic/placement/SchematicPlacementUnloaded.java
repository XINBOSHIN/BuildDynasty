/*
 * This file is part of builddynasty.
 *
 * builddynasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * builddynasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with builddynasty.  If not, see <https://www.gnu.org/licenses/>.
 */

package fi.dy.masa.litematica.schematic.placement;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.io.File;

public class SchematicPlacementUnloaded {
    protected String name = "?";
    @Nullable
    protected File schematicFile;
    protected BlockPos origin = BlockPos.ORIGIN;

    public String getName() {
        return this.name;
    }

    @Nullable
    public File getSchematicFile() {
        return this.schematicFile;
    }

    public BlockPos getOrigin() {
        return this.origin;
    }
}
