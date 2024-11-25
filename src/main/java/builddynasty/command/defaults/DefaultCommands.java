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
import BuildDynasty.api.command.ICommand;

import java.util.*;

public final class DefaultCommands {

    private DefaultCommands() {
    }

    public static List<ICommand> createAll(IBuildDynasty BuildDynasty) {
        Objects.requireNonNull(BuildDynasty);
        List<ICommand> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(BuildDynasty),
                new SetCommand(BuildDynasty),
                new CommandAlias(BuildDynasty, Arrays.asList("modified", "mod", "BuildDynasty", "modifiedsettings"), "List modified settings", "set modified"),
                new CommandAlias(BuildDynasty, "reset", "Reset all settings or just one", "set reset"),
                new GoalCommand(BuildDynasty),
                new GotoCommand(BuildDynasty),
                new PathCommand(BuildDynasty),
                new ProcCommand(BuildDynasty),
                new ETACommand(BuildDynasty),
                new VersionCommand(BuildDynasty),
                new RepackCommand(BuildDynasty),
                new BuildCommand(BuildDynasty),
                new SchematicaCommand(BuildDynasty),
                new LitematicaCommand(BuildDynasty),
                new ComeCommand(BuildDynasty),
                new AxisCommand(BuildDynasty),
                new ForceCancelCommand(BuildDynasty),
                new GcCommand(BuildDynasty),
                new InvertCommand(BuildDynasty),
                new TunnelCommand(BuildDynasty),
                new RenderCommand(BuildDynasty),
                new FarmCommand(BuildDynasty),
                new FollowCommand(BuildDynasty),
                new ExploreFilterCommand(BuildDynasty),
                new ReloadAllCommand(BuildDynasty),
                new SaveAllCommand(BuildDynasty),
                new ExploreCommand(BuildDynasty),
                new BlacklistCommand(BuildDynasty),
                new FindCommand(BuildDynasty),
                new MineCommand(BuildDynasty),
                new ClickCommand(BuildDynasty),
                new SurfaceCommand(BuildDynasty),
                new ThisWayCommand(BuildDynasty),
                new WaypointsCommand(BuildDynasty),
                new CommandAlias(BuildDynasty, "sethome", "Sets your home waypoint", "waypoints save home"),
                new CommandAlias(BuildDynasty, "home", "Path to your home waypoint", "waypoints goto home"),
                new SelCommand(BuildDynasty),
                new ElytraCommand(BuildDynasty)
        ));
        ExecutionControlCommands prc = new ExecutionControlCommands(BuildDynasty);
        commands.add(prc.pauseCommand);
        commands.add(prc.resumeCommand);
        commands.add(prc.pausedCommand);
        commands.add(prc.cancelCommand);
        return Collections.unmodifiableList(commands);
    }
}
