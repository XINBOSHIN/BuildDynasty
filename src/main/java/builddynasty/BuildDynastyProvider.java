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

package BuildDynasty;

import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.IBuildDynastyProvider;
import BuildDynasty.api.cache.IWorldScanner;
import BuildDynasty.api.command.ICommandSystem;
import BuildDynasty.api.schematic.ISchematicSystem;
import BuildDynasty.cache.FasterWorldScanner;
import BuildDynasty.command.CommandSystem;
import BuildDynasty.command.ExampleBuildDynastyControl;
import BuildDynasty.utils.schematic.SchematicSystem;
import net.minecraft.client.Minecraft;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author XINBOSHIN
 * @since 9/29/2018
 */
public final class BuildDynastyProvider implements IBuildDynastyProvider {

    private final List<IBuildDynasty> all;
    private final List<IBuildDynasty> allView;

    public BuildDynastyProvider() {
        this.all = new CopyOnWriteArrayList<>();
        this.allView = Collections.unmodifiableList(this.all);

        // Setup chat control, just for the primary instance
        final BuildDynasty primary = (BuildDynasty) this.createBuildDynasty(Minecraft.getMinecraft());
        primary.registerBehavior(ExampleBuildDynastyControl::new);
    }

    @Override
    public IBuildDynasty getPrimaryBuildDynasty() {
        return this.all.get(0);
    }

    @Override
    public List<IBuildDynasty> getAllBuildDynastys() {
        return this.allView;
    }

    @Override
    public synchronized IBuildDynasty createBuildDynasty(Minecraft minecraft) {
        IBuildDynasty BuildDynasty = this.getBuildDynastyForMinecraft(minecraft);
        if (BuildDynasty == null) {
            this.all.add(BuildDynasty = new BuildDynasty(minecraft));
        }
        return BuildDynasty;
    }

    @Override
    public synchronized boolean destroyBuildDynasty(IBuildDynasty BuildDynasty) {
        return BuildDynasty != this.getPrimaryBuildDynasty() && this.all.remove(BuildDynasty);
    }

    @Override
    public IWorldScanner getWorldScanner() {
        return FasterWorldScanner.INSTANCE;
    }

    @Override
    public ICommandSystem getCommandSystem() {
        return CommandSystem.INSTANCE;
    }

    @Override
    public ISchematicSystem getSchematicSystem() {
        return SchematicSystem.INSTANCE;
    }
}
