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

package BuildDynasty.command.defaults;

import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.command.Command;
import BuildDynasty.api.command.argument.IArgConsumer;
import BuildDynasty.api.command.exception.CommandException;
import BuildDynasty.api.command.exception.CommandInvalidStateException;
import BuildDynasty.api.pathing.goals.Goal;
import BuildDynasty.api.pathing.goals.GoalInverted;
import BuildDynasty.api.process.ICustomGoalProcess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class InvertCommand extends Command {

    public InvertCommand(IBuildDynasty BuildDynasty) {
        super(BuildDynasty, "invert");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(0);
        ICustomGoalProcess customGoalProcess = BuildDynasty.getCustomGoalProcess();
        Goal goal;
        if ((goal = customGoalProcess.getGoal()) == null) {
            throw new CommandInvalidStateException("No goal");
        }
        if (goal instanceof GoalInverted) {
            goal = ((GoalInverted) goal).origin;
        } else {
            goal = new GoalInverted(goal);
        }
        customGoalProcess.setGoalAndPath(goal);
        logDirect(String.format("Goal: %s", goal.toString()));
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Run away from the current goal";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "The invert command tells BuildDynasty to head away from the current goal rather than towards it.",
                "",
                "Usage:",
                "> invert - Invert the current goal."
        );
    }
}
