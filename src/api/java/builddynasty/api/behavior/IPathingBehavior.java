
package builddynasty.api.behavior;

import builddynasty.api.pathing.calc.IPath;
import builddynasty.api.pathing.calc.IPathFinder;
import builddynasty.api.pathing.goals.Goal;
import builddynasty.api.pathing.path.IPathExecutor;

import java.util.Optional;


public interface IPathingBehavior extends IBehavior {


    default Optional<Double> ticksRemainingInSegment() {
        return ticksRemainingInSegment(true);
    }


    default Optional<Double> ticksRemainingInSegment(boolean includeCurrentMovement) {
        IPathExecutor current = getCurrent();
        if (current == null) {
            return Optional.empty();
        }
        int start = includeCurrentMovement ? current.getPosition() : current.getPosition() + 1;
        return Optional.of(current.getPath().ticksRemainingFrom(start));
    }


    Optional<Double> estimatedTicksToGoal();


    Goal getGoal();


    boolean isPathing();


    default boolean hasPath() {
        return getCurrent() != null;
    }


    boolean cancelEverything();


    void forceCancel();


    default Optional<IPath> getPath() {
        return Optional.ofNullable(getCurrent()).map(IPathExecutor::getPath);
    }


    Optional<? extends IPathFinder> getInProgress();


    IPathExecutor getCurrent();


    IPathExecutor getNext();
}
