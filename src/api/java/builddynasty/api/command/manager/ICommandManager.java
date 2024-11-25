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

package BuildDynasty.api.command.manager;

import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.command.ICommand;
import BuildDynasty.api.command.argument.ICommandArgument;
import BuildDynasty.api.command.registry.Registry;
import net.minecraft.util.Tuple;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author XINBOSHIN
 * @since 9/21/2019
 */
public interface ICommandManager {

    IBuildDynasty getBuildDynasty();

    Registry<ICommand> getRegistry();

    /**
     * @param name The command name to search for.
     * @return The command, if found.
     */
    ICommand getCommand(String name);

    boolean execute(String string);

    boolean execute(Tuple<String, List<ICommandArgument>> expanded);

    Stream<String> tabComplete(Tuple<String, List<ICommandArgument>> expanded);

    Stream<String> tabComplete(String prefix);
}
