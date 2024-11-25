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
import BuildDynasty.api.cache.IWaypoint;
import BuildDynasty.api.cache.IWaypointCollection;
import BuildDynasty.api.command.exception.CommandException;
import BuildDynasty.api.command.helpers.TabCompleteHelper;

import java.util.Comparator;
import java.util.stream.Stream;

public enum ForWaypoints implements IDatatypeFor<IWaypoint[]> {
    INSTANCE;

    @Override
    public IWaypoint[] get(IDatatypeContext ctx) throws CommandException {
        final String input = ctx.getConsumer().getString();
        final IWaypoint.Tag tag = IWaypoint.Tag.getByName(input);

        // If the input doesn't resolve to a valid tag, resolve by name
        return tag == null
                ? getWaypointsByName(ctx.getBuildDynasty(), input)
                : getWaypointsByTag(ctx.getBuildDynasty(), tag);
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        return new TabCompleteHelper()
                .append(getWaypointNames(ctx.getBuildDynasty()))
                .sortAlphabetically()
                .prepend(IWaypoint.Tag.getAllNames())
                .filterPrefix(ctx.getConsumer().getString())
                .stream();
    }

    public static IWaypointCollection waypoints(IBuildDynasty BuildDynasty) {
        return BuildDynasty.getWorldProvider().getCurrentWorld().getWaypoints();
    }

    public static IWaypoint[] getWaypoints(IBuildDynasty BuildDynasty) {
        return waypoints(BuildDynasty).getAllWaypoints().stream()
                .sorted(Comparator.comparingLong(IWaypoint::getCreationTimestamp).reversed())
                .toArray(IWaypoint[]::new);
    }

    public static String[] getWaypointNames(IBuildDynasty BuildDynasty) {
        return Stream.of(getWaypoints(BuildDynasty))
                .map(IWaypoint::getName)
                .filter(name -> !name.isEmpty())
                .toArray(String[]::new);
    }

    public static IWaypoint[] getWaypointsByTag(IBuildDynasty BuildDynasty, IWaypoint.Tag tag) {
        return waypoints(BuildDynasty).getByTag(tag).stream()
                .sorted(Comparator.comparingLong(IWaypoint::getCreationTimestamp).reversed())
                .toArray(IWaypoint[]::new);
    }

    public static IWaypoint[] getWaypointsByName(IBuildDynasty BuildDynasty, String name) {
        return Stream.of(getWaypoints(BuildDynasty))
                .filter(waypoint -> waypoint.getName().equalsIgnoreCase(name))
                .toArray(IWaypoint[]::new);
    }
}
