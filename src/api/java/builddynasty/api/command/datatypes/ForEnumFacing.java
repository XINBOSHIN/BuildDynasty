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

import BuildDynasty.api.command.exception.CommandException;
import BuildDynasty.api.command.helpers.TabCompleteHelper;
import net.minecraft.util.EnumFacing;

import java.util.Locale;
import java.util.stream.Stream;

public enum ForEnumFacing implements IDatatypeFor<EnumFacing> {
    INSTANCE;

    @Override
    public EnumFacing get(IDatatypeContext ctx) throws CommandException {
        return EnumFacing.valueOf(ctx.getConsumer().getString().toUpperCase(Locale.US));
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        return new TabCompleteHelper()
                .append(Stream.of(EnumFacing.values())
                        .map(EnumFacing::getName).map(String::toLowerCase))
                .filterPrefix(ctx.getConsumer().getString())
                .stream();
    }
}
