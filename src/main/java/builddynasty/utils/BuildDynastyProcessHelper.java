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

package BuildDynasty.utils;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.process.IBuildDynastyProcess;
import BuildDynasty.api.utils.Helper;
import BuildDynasty.api.utils.IPlayerContext;

public abstract class BuildDynastyProcessHelper implements IBuildDynastyProcess, Helper {

    protected final BuildDynasty BuildDynasty;
    protected final IPlayerContext ctx;

    public BuildDynastyProcessHelper(BuildDynasty BuildDynasty) {
        this.BuildDynasty = BuildDynasty;
        this.ctx = BuildDynasty.getPlayerContext();
    }

    @Override
    public boolean isTemporary() {
        return false;
    }
}
