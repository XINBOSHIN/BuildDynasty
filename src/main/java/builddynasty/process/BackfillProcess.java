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

package BuildDynasty.process;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.process.PathingCommand;
import BuildDynasty.api.process.PathingCommandType;
import BuildDynasty.api.utils.input.Input;
import BuildDynasty.pathing.movement.Movement;
import BuildDynasty.pathing.movement.MovementHelper;
import BuildDynasty.pathing.movement.MovementState;
import BuildDynasty.pathing.path.PathExecutor;
import BuildDynasty.utils.BuildDynastyProcessHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.EmptyChunk;

import java.util.*;
import java.util.stream.Collectors;

public final class BackfillProcess extends BuildDynastyProcessHelper {

    public HashMap<BlockPos, IBlockState> blocksToReplace = new HashMap<>();

    public BackfillProcess(BuildDynasty BuildDynasty) {
        super(BuildDynasty);
    }

    @Override
    public boolean isActive() {
        if (ctx.player() == null || ctx.world() == null) {
            return false;
        }
        if (!BuildDynasty.settings().backfill.value) {
            return false;
        }
        if (BuildDynasty.settings().allowParkour.value) {
            logDirect("Backfill cannot be used with allowParkour true");
            BuildDynasty.settings().backfill.value = false;
            return false;
        }
        for (BlockPos pos : new ArrayList<>(blocksToReplace.keySet())) {
            if (ctx.world().getChunk(pos) instanceof EmptyChunk || ctx.world().getBlockState(pos).getBlock() != Blocks.AIR) {
                blocksToReplace.remove(pos);
            }
        }
        amIBreakingABlockHMMMMMMM();
        BuildDynasty.getInputOverrideHandler().clearAllKeys();

        return !toFillIn().isEmpty();
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        if (!isSafeToCancel) {
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        BuildDynasty.getInputOverrideHandler().clearAllKeys();
        for (BlockPos toPlace : toFillIn()) {
            MovementState fake = new MovementState();
            switch (MovementHelper.attemptToPlaceABlock(fake, BuildDynasty, toPlace, false, false)) {
                case NO_OPTION:
                    continue;
                case READY_TO_PLACE:
                    BuildDynasty.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
                    return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
                case ATTEMPTING:
                    // patience
                    BuildDynasty.getLookBehavior().updateTarget(fake.getTarget().getRotation().get(), true);
                    return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
                default:
                    throw new IllegalStateException();
            }
        }
        return new PathingCommand(null, PathingCommandType.DEFER); // cede to other process
    }

    private void amIBreakingABlockHMMMMMMM() {
        if (!ctx.getSelectedBlock().isPresent() || !BuildDynasty.getPathingBehavior().isPathing()) {
            return;
        }
        blocksToReplace.put(ctx.getSelectedBlock().get(), ctx.world().getBlockState(ctx.getSelectedBlock().get()));
    }

    public List<BlockPos> toFillIn() {
        return blocksToReplace
                .keySet()
                .stream()
                .filter(pos -> ctx.world().getBlockState(pos).getBlock() == Blocks.AIR)
                .filter(pos -> ctx.world().mayPlace(Blocks.DIRT, pos, false, EnumFacing.UP, null))
                .filter(pos -> !partOfCurrentMovement(pos))
                .sorted(Comparator.<BlockPos>comparingDouble(ctx.player()::getDistanceSq).reversed())
                .collect(Collectors.toList());
    }

    private boolean partOfCurrentMovement(BlockPos pos) {
        PathExecutor exec = BuildDynasty.getPathingBehavior().getCurrent();
        if (exec == null || exec.finished() || exec.failed()) {
            return false;
        }
        Movement movement = (Movement) exec.getPath().movements().get(exec.getPosition());
        return Arrays.asList(movement.toBreakAll()).contains(pos);
    }

    @Override
    public void onLostControl() {
        if (blocksToReplace != null && !blocksToReplace.isEmpty()) {
            blocksToReplace.clear();
        }
    }

    @Override
    public String displayName0() {
        return "Backfill";
    }

    @Override
    public boolean isTemporary() {
        return true;
    }

    @Override
    public double priority() {
        return 5;
    }
}
