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

package BuildDynasty.cache;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.cache.IWorldProvider;
import BuildDynasty.api.utils.IPlayerContext;
import BuildDynasty.utils.accessor.IAnvilChunkLoader;
import BuildDynasty.utils.accessor.IChunkProviderServer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author XINBOSHIN
 * @since 8/4/2018
 */
public class WorldProvider implements IWorldProvider {

    private static final Map<Path, WorldData> worldCache = new HashMap<>();

    private final BuildDynasty BuildDynasty;
    private final IPlayerContext ctx;
    private WorldData currentWorld;

    /**
     * This lets us detect a broken load/unload hook.
     * @see #detectAndHandleBrokenLoading()
     */
    private World mcWorld;

    public WorldProvider(BuildDynasty BuildDynasty) {
        this.BuildDynasty = BuildDynasty;
        this.ctx = BuildDynasty.getPlayerContext();
    }

    @Override
    public final WorldData getCurrentWorld() {
        this.detectAndHandleBrokenLoading();
        return this.currentWorld;
    }

    /**
     * Called when a new world is initialized to discover the
     *
     * @param world The new world
     */
    public final void initWorld(World world) {
        this.getSaveDirectories(world).ifPresent(dirs -> {
            final Path worldDir = dirs.getFirst();
            final Path readmeDir = dirs.getSecond();

            try {
                // lol wtf is this BuildDynasty folder in my minecraft save?
                // good thing we have a readme
                Files.createDirectories(readmeDir);
                Files.write(
                        readmeDir.resolve("readme.txt"),
                        "https://github.com/cabaletta/BuildDynasty\n".getBytes(StandardCharsets.US_ASCII)
                );
            } catch (IOException ignored) {}

            // We will actually store the world data in a subfolder: "DIM<id>"
            final Path worldDataDir = this.getWorldDataDirectory(worldDir, world);
            try {
                Files.createDirectories(worldDataDir);
            } catch (IOException ignored) {}

            System.out.println("BuildDynasty world data dir: " + worldDataDir);
            synchronized (worldCache) {
                final int dimension = world.provider.getDimensionType().getId();
                this.currentWorld = worldCache.computeIfAbsent(worldDataDir, d -> new WorldData(d, dimension));
            }
            this.mcWorld = ctx.world();
        });
    }

    public final void closeWorld() {
        WorldData world = this.currentWorld;
        this.currentWorld = null;
        this.mcWorld = null;
        if (world == null) {
            return;
        }
        world.onClose();
    }

    private Path getWorldDataDirectory(Path parent, World world) {
        return parent.resolve("DIM" + world.provider.getDimensionType().getId());
    }

    /**
     * @param world The world
     * @return An {@link Optional} containing the world's BuildDynasty dir and readme dir, or {@link Optional#empty()} if
     *         the world isn't valid for caching.
     */
    private Optional<Tuple<Path, Path>> getSaveDirectories(World world) {
        Path worldDir;
        Path readmeDir;

        // If there is an integrated server running (Aka Singleplayer) then do magic to find the world save file
        if (ctx.minecraft().isSingleplayer()) {
            final int dimension = world.provider.getDimensionType().getId();
            final WorldServer localServerWorld = ctx.minecraft().getIntegratedServer().getWorld(dimension);
            final IChunkProviderServer provider = (IChunkProviderServer) localServerWorld.getChunkProvider();
            final IAnvilChunkLoader loader = (IAnvilChunkLoader) provider.getChunkLoader();
            worldDir = loader.getChunkSaveLocation().toPath();

            // Gets the "depth" of this directory relative to the game's run directory, 2 is the location of the world
            if (worldDir.relativize(ctx.minecraft().gameDir.toPath()).getNameCount() != 2) {
                // subdirectory of the main save directory for this world
                worldDir = worldDir.getParent();
            }

            worldDir = worldDir.resolve("BuildDynasty");
            readmeDir = worldDir;
        } else { // Otherwise, the server must be remote...
            String folderName;
            final ServerData serverData = ctx.minecraft().getCurrentServerData();
            if (serverData != null) {
                folderName = serverData.serverIP;
            } else {
                //replaymod causes null currentServerData and false singleplayer.
                System.out.println("World seems to be a replay. Not loading BuildDynasty cache.");
                currentWorld = null;
                mcWorld = ctx.world();
                return Optional.empty();
            }
            if (SystemUtils.IS_OS_WINDOWS) {
                folderName = folderName.replace(":", "_");
            }
            // TODO: This should probably be in "BuildDynasty/servers"
            worldDir = BuildDynasty.getDirectory().resolve(folderName);
            // Just write the readme to the BuildDynasty directory instead of each server save in it
            readmeDir = BuildDynasty.getDirectory();
        }

        return Optional.of(new Tuple<>(worldDir, readmeDir));
    }

    /**
     * Why does this exist instead of fixing the event? Some mods break the event. Lol.
     */
    private void detectAndHandleBrokenLoading() {
        if (this.mcWorld != ctx.world()) {
            if (this.currentWorld != null) {
                System.out.println("mc.world unloaded unnoticed! Unloading BuildDynasty cache now.");
                closeWorld();
            }
            if (ctx.world() != null) {
                System.out.println("mc.world loaded unnoticed! Loading BuildDynasty cache now.");
                initWorld(ctx.world());
            }
        } else if (this.currentWorld == null && ctx.world() != null && (ctx.minecraft().isSingleplayer() || ctx.minecraft().getCurrentServerData() != null)) {
            System.out.println("Retrying to load BuildDynasty cache");
            initWorld(ctx.world());
        }
    }
}
