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

package BuildDynasty.command.defaults;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.command.Command;
import BuildDynasty.api.command.argument.IArgConsumer;
import BuildDynasty.api.command.datatypes.RelativeBlockPos;
import BuildDynasty.api.command.datatypes.RelativeFile;
import BuildDynasty.api.command.exception.CommandException;
import BuildDynasty.api.command.exception.CommandInvalidStateException;
import BuildDynasty.api.utils.BetterBlockPos;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BuildCommand extends Command {

    private final File schematicsDir;

    public BuildCommand(IBuildDynasty BuildDynasty) {
        super(BuildDynasty, "build");
        this.schematicsDir = new File(BuildDynasty.getPlayerContext().minecraft().gameDir, "schematics");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        File file = args.getDatatypePost(RelativeFile.INSTANCE, schematicsDir).getAbsoluteFile();
        if (FilenameUtils.getExtension(file.getAbsolutePath()).isEmpty()) {
            file = new File(file.getAbsolutePath() + "." + BuildDynasty.settings().schematicFallbackExtension.value);
        }
        BetterBlockPos origin = ctx.playerFeet();
        BetterBlockPos buildOrigin;
        if (args.hasAny()) {
            args.requireMax(3);
            buildOrigin = args.getDatatypePost(RelativeBlockPos.INSTANCE, origin);
        } else {
            args.requireMax(0);
            buildOrigin = origin;
        }
        boolean success = BuildDynasty.getBuilderProcess().build(file.getName(), file, buildOrigin);
        if (!success) {
            throw new CommandInvalidStateException("Couldn't load the schematic. Make sure to use the FULL file name, including the extension (e.g. blah.schematic).");
        }
        logDirect(String.format("Successfully loaded schematic for building\nOrigin: %s", buildOrigin));
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        if (args.hasExactlyOne()) {
            return RelativeFile.tabComplete(args, schematicsDir);
        } else if (args.has(2)) {
            args.get();
            return args.tabCompleteDatatype(RelativeBlockPos.INSTANCE);
        }
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Build a schematic";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "Build a schematic from a file.",
                "",
                "Usage:",
                "> build <filename> - Loads and builds '<filename>.schematic'",
                "> build <filename> <x> <y> <z> - Custom position"
        );
    }
}
