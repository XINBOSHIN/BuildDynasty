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

package BuildDynasty.api.command;

import BuildDynasty.api.command.argument.IArgConsumer;
import BuildDynasty.api.command.exception.CommandException;
import BuildDynasty.api.utils.Helper;

import java.util.List;
import java.util.stream.Stream;

/**
 * The base for a command.
 *
 * @author XINBOSHIN
 * @since 10/7/2019
 */
public interface ICommand extends Helper {

    /**
     * Called when this command is executed.
     */
    void execute(String label, IArgConsumer args) throws CommandException;

    /**
     * Called when the command needs to tab complete. Return a Stream representing the entries to put in the completions
     * list.
     */
    Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException;

    /**
     * @return A <b>single-line</b> string containing a short description of this command's purpose.
     */
    String getShortDesc();

    /**
     * @return A list of lines that will be printed by the help command when the user wishes to view them.
     */
    List<String> getLongDesc();

    /**
     * @return A list of the names that can be accepted to have arguments passed to this command
     */
    List<String> getNames();

    /**
     * @return {@code true} if this command should be hidden from the help menu
     */
    default boolean hiddenFromHelp() {
        return false;
    }
}
