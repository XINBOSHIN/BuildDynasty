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
import BuildDynasty.utils.BuildDynastyProcessHelper;

public class InventoryPauserProcess extends BuildDynastyProcessHelper {

    boolean pauseRequestedLastTick;
    boolean safeToCancelLastTick;
    int ticksOfStationary;

    public InventoryPauserProcess(BuildDynasty BuildDynasty) {
        super(BuildDynasty);
    }

    @Override
    public boolean isActive() {
        if (ctx.player() == null || ctx.world() == null) {
            return false;
        }
        return true;
    }

    private double motion() {
        return Math.sqrt(ctx.player().motionX * ctx.player().motionX + ctx.player().motionZ * ctx.player().motionZ);
    }

    private boolean stationaryNow() {
        return motion() < 0.00001;
    }

    public boolean stationaryForInventoryMove() {
        pauseRequestedLastTick = true;
        return safeToCancelLastTick && ticksOfStationary > 1;
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        //logDebug(pauseRequestedLastTick + " " + safeToCancelLastTick + " " + ticksOfStationary);
        safeToCancelLastTick = isSafeToCancel;
        if (pauseRequestedLastTick) {
            pauseRequestedLastTick = false;
            if (stationaryNow()) {
                ticksOfStationary++;
            }
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        ticksOfStationary = 0;
        return new PathingCommand(null, PathingCommandType.DEFER);
    }

    @Override
    public void onLostControl() {

    }

    @Override
    public String displayName0() {
        return "inventory pauser";
    }

    @Override
    public double priority() {
        return 5.1; // slightly higher than backfill
    }

    @Override
    public boolean isTemporary() {
        return true;
    }
}
