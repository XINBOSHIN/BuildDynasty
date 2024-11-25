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
import BuildDynasty.api.pathing.goals.Goal;
import BuildDynasty.api.pathing.goals.GoalComposite;
import BuildDynasty.api.pathing.goals.GoalNear;
import BuildDynasty.api.pathing.goals.GoalXZ;
import BuildDynasty.api.process.IFollowProcess;
import BuildDynasty.api.process.PathingCommand;
import BuildDynasty.api.process.PathingCommandType;
import BuildDynasty.utils.BuildDynastyProcessHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Follow an entity
 *
 * @author leijurv
 */
public final class FollowProcess extends BuildDynastyProcessHelper implements IFollowProcess {

    private Predicate<Entity> filter;
    private List<Entity> cache;

    public FollowProcess(BuildDynasty BuildDynasty) {
        super(BuildDynasty);
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        scanWorld();
        Goal goal = new GoalComposite(cache.stream().map(this::towards).toArray(Goal[]::new));
        return new PathingCommand(goal, PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }

    private Goal towards(Entity following) {
        BlockPos pos;
        if (BuildDynasty.settings().followOffsetDistance.value == 0) {
            pos = new BlockPos(following);
        } else {
            GoalXZ g = GoalXZ.fromDirection(following.getPositionVector(), BuildDynasty.settings().followOffsetDirection.value, BuildDynasty.settings().followOffsetDistance.value);
            pos = new BlockPos(g.getX(), following.posY, g.getZ());
        }
        return new GoalNear(pos, BuildDynasty.settings().followRadius.value);
    }


    private boolean followable(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity.equals(ctx.player())) {
            return false;
        }
        return ctx.world().loadedEntityList.contains(entity);
    }

    private void scanWorld() {
        cache = Stream.of(ctx.world().loadedEntityList, ctx.world().playerEntities)
                .flatMap(List::stream)
                .filter(this::followable)
                .filter(this.filter)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean isActive() {
        if (filter == null) {
            return false;
        }
        scanWorld();
        return !cache.isEmpty();
    }

    @Override
    public void onLostControl() {
        filter = null;
        cache = null;
    }

    @Override
    public String displayName0() {
        return "Following " + cache;
    }

    @Override
    public void follow(Predicate<Entity> filter) {
        this.filter = filter;
    }

    @Override
    public List<Entity> following() {
        return cache;
    }

    @Override
    public Predicate<Entity> currentFilter() {
        return filter;
    }
}
