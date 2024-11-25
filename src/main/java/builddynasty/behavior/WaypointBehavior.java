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

package BuildDynasty.behavior;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.cache.IWaypoint;
import BuildDynasty.api.cache.Waypoint;
import BuildDynasty.api.event.events.BlockInteractEvent;
import BuildDynasty.api.utils.BetterBlockPos;
import BuildDynasty.api.utils.Helper;
import BuildDynasty.utils.BlockStateInterface;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Set;

import static BuildDynasty.api.command.IBuildDynastyChatControl.FORCE_COMMAND_PREFIX;

public class WaypointBehavior extends Behavior {


    public WaypointBehavior(BuildDynasty BuildDynasty) {
        super(BuildDynasty);
    }

    @Override
    public void onBlockInteract(BlockInteractEvent event) {
        if (!BuildDynasty.settings().doBedWaypoints.value)
            return;
        if (event.getType() == BlockInteractEvent.Type.USE) {
            BetterBlockPos pos = BetterBlockPos.from(event.getPos());
            IBlockState state = BlockStateInterface.get(ctx, pos);
            if (state.getBlock() instanceof BlockBed) {
                if (state.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT) {
                    pos = pos.offset(state.getValue(BlockBed.FACING));
                }
                Set<IWaypoint> waypoints = BuildDynasty.getWorldProvider().getCurrentWorld().getWaypoints().getByTag(IWaypoint.Tag.BED);
                boolean exists = waypoints.stream().map(IWaypoint::getLocation).filter(pos::equals).findFirst().isPresent();
                if (!exists) {
                    BuildDynasty.getWorldProvider().getCurrentWorld().getWaypoints().addWaypoint(new Waypoint("bed", Waypoint.Tag.BED, pos));
                }
            }
        }
    }

    @Override
    public void onPlayerDeath() {
        if (!BuildDynasty.settings().doDeathWaypoints.value)
            return;
        Waypoint deathWaypoint = new Waypoint("death", Waypoint.Tag.DEATH, ctx.playerFeet());
        BuildDynasty.getWorldProvider().getCurrentWorld().getWaypoints().addWaypoint(deathWaypoint);
        ITextComponent component = new TextComponentString("Death position saved.");
        component.getStyle()
                .setColor(TextFormatting.WHITE)
                .setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new TextComponentString("Click to goto death")
                ))
                .setClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        String.format(
                                "%s%s goto %s @ %d",
                                FORCE_COMMAND_PREFIX,
                                "wp",
                                deathWaypoint.getTag().getName(),
                                deathWaypoint.getCreationTimestamp()
                        )
                ));
        Helper.HELPER.logDirect(component);
    }

}
