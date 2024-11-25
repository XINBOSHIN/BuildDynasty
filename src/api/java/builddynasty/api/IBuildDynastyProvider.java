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

package BuildDynasty.api;

import BuildDynasty.api.cache.IWorldScanner;
import BuildDynasty.api.command.ICommand;
import BuildDynasty.api.command.ICommandSystem;
import BuildDynasty.api.schematic.ISchematicSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.util.List;
import java.util.Objects;

/**
 * Provides the present {@link IBuildDynasty} instances, as well as non-BuildDynasty instance related APIs.
 *
 * @author leijurv
 */
public interface IBuildDynastyProvider {

    /**
     * Returns the primary {@link IBuildDynasty} instance. This instance is persistent, and
     * is represented by the local player that is created by the game itself, not a "bot"
     * player through BuildDynasty.
     *
     * @return The primary {@link IBuildDynasty} instance.
     */
    IBuildDynasty getPrimaryBuildDynasty();

    /**
     * Returns all of the active {@link IBuildDynasty} instances. This includes the local one
     * returned by {@link #getPrimaryBuildDynasty()}.
     *
     * @return All active {@link IBuildDynasty} instances.
     * @see #getBuildDynastyForPlayer(EntityPlayerSP)
     */
    List<IBuildDynasty> getAllBuildDynastys();

    /**
     * Provides the {@link IBuildDynasty} instance for a given {@link EntityPlayerSP}.
     *
     * @param player The player
     * @return The {@link IBuildDynasty} instance.
     */
    default IBuildDynasty getBuildDynastyForPlayer(EntityPlayerSP player) {
        for (IBuildDynasty BuildDynasty : this.getAllBuildDynastys()) {
            if (Objects.equals(player, BuildDynasty.getPlayerContext().player())) {
                return BuildDynasty;
            }
        }
        return null;
    }

    /**
     * Provides the {@link IBuildDynasty} instance for a given {@link Minecraft}.
     *
     * @param minecraft The minecraft
     * @return The {@link IBuildDynasty} instance.
     */
    default IBuildDynasty getBuildDynastyForMinecraft(Minecraft minecraft) {
        for (IBuildDynasty BuildDynasty : this.getAllBuildDynastys()) {
            if (Objects.equals(minecraft, BuildDynasty.getPlayerContext().minecraft())) {
                return BuildDynasty;
            }
        }
        return null;
    }

    /**
     * Provides the {@link IBuildDynasty} instance for the player with the specified connection.
     *
     * @param connection The connection
     * @return The {@link IBuildDynasty} instance.
     */
    default IBuildDynasty getBuildDynastyForConnection(NetHandlerPlayClient connection) {
        for (IBuildDynasty BuildDynasty : this.getAllBuildDynastys()) {
            final EntityPlayerSP player = BuildDynasty.getPlayerContext().player();
            if (player != null && player.connection == connection) {
                return BuildDynasty;
            }
        }
        return null;
    }

    /**
     * Creates and registers a new {@link IBuildDynasty} instance using the specified {@link Minecraft}. The existing
     * instance is returned if already registered.
     *
     * @param minecraft The minecraft
     * @return The {@link IBuildDynasty} instance
     */
    IBuildDynasty createBuildDynasty(Minecraft minecraft);

    /**
     * Destroys and removes the specified {@link IBuildDynasty} instance. If the specified instance is the
     * {@link #getPrimaryBuildDynasty() primary BuildDynasty}, this operation has no effect and will return {@code false}.
     *
     * @param BuildDynasty The BuildDynasty instance to remove
     * @return Whether the BuildDynasty instance was removed
     */
    boolean destroyBuildDynasty(IBuildDynasty BuildDynasty);

    /**
     * Returns the {@link IWorldScanner} instance. This is not a type returned by
     * {@link IBuildDynasty} implementation, because it is not linked with {@link IBuildDynasty}.
     *
     * @return The {@link IWorldScanner} instance.
     */
    IWorldScanner getWorldScanner();

    /**
     * Returns the {@link ICommandSystem} instance. This is not bound to a specific {@link IBuildDynasty}
     * instance because {@link ICommandSystem} itself controls global behavior for {@link ICommand}s.
     *
     * @return The {@link ICommandSystem} instance.
     */
    ICommandSystem getCommandSystem();

    /**
     * @return The {@link ISchematicSystem} instance.
     */
    ISchematicSystem getSchematicSystem();
}
