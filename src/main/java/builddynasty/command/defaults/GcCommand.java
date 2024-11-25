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

import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.command.Command;
import BuildDynasty.api.command.argument.IArgConsumer;
import BuildDynasty.api.command.exception.CommandException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GcCommand extends Command {

    public GcCommand(IBuildDynasty BuildDynasty) {
        super(BuildDynasty, "gc");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(0);
        System.gc();
        logDirect("ok called System.gc()");
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Call System.gc()";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "Calls System.gc().",
                "",
                "Usage:",
                "> gc"
        );
    }
}
