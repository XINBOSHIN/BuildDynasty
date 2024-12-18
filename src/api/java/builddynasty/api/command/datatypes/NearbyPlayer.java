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
import BuildDynasty.api.command.exception.CommandException;
import BuildDynasty.api.command.helpers.TabCompleteHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.stream.Stream;

/**
 * An {@link IDatatype} used to resolve nearby players, those within
 * render distance of the target {@link IBuildDynasty} instance.
 */
public enum NearbyPlayer implements IDatatypeFor<EntityPlayer> {
    INSTANCE;

    @Override
    public EntityPlayer get(IDatatypeContext ctx) throws CommandException {
        final String username = ctx.getConsumer().getString();
        return getPlayers(ctx).stream()
                .filter(s -> s.getName().equalsIgnoreCase(username))
                .findFirst().orElse(null);
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        return new TabCompleteHelper()
                .append(getPlayers(ctx).stream().map(EntityPlayer::getName))
                .filterPrefix(ctx.getConsumer().getString())
                .sortAlphabetically()
                .stream();
    }

    private static List<EntityPlayer> getPlayers(IDatatypeContext ctx) {
        return ctx.getBuildDynasty().getPlayerContext().world().playerEntities;
    }
}
