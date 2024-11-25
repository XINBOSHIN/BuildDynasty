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

import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.utils.IPlayerContext;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A default implementation of {@link ICommand} which provides easy access to the
 * command's bound {@link IBuildDynasty} instance, {@link IPlayerContext} and an easy
 * way to provide multiple valid command execution names through the default constructor.
 * <p>
 * So basically, you should use it because it provides a small amount of boilerplate,
 * but you're not forced to use it.
 *
 * @author LoganDark
 * @see ICommand
 */
public abstract class Command implements ICommand {

    protected IBuildDynasty BuildDynasty;
    protected IPlayerContext ctx;

    /**
     * The names of this command. This is what you put after the command prefix.
     */
    protected final List<String> names;

    /**
     * Creates a new BuildDynasty control command.
     *
     * @param names The names of this command. This is what you put after the command prefix.
     */
    protected Command(IBuildDynasty BuildDynasty, String... names) {
        this.names = Collections.unmodifiableList(Stream.of(names)
                .map(s -> s.toLowerCase(Locale.US))
                .collect(Collectors.toList()));
        this.BuildDynasty = BuildDynasty;
        this.ctx = BuildDynasty.getPlayerContext();
    }

    @Override
    public final List<String> getNames() {
        return this.names;
    }
}
