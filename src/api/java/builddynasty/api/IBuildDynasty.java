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

import BuildDynasty.api.behavior.ILookBehavior;
import BuildDynasty.api.behavior.IPathingBehavior;
import BuildDynasty.api.cache.IWorldProvider;
import BuildDynasty.api.command.manager.ICommandManager;
import BuildDynasty.api.event.listener.IEventBus;
import BuildDynasty.api.pathing.calc.IPathingControlManager;
import BuildDynasty.api.process.*;
import BuildDynasty.api.selection.ISelectionManager;
import BuildDynasty.api.utils.IInputOverrideHandler;
import BuildDynasty.api.utils.IPlayerContext;

/**
 * @author XINBOSHIN
 * @since 9/29/2018
 */
public interface IBuildDynasty {

    /**
     * @return The {@link IPathingBehavior} instance
     * @see IPathingBehavior
     */
    IPathingBehavior getPathingBehavior();

    /**
     * @return The {@link ILookBehavior} instance
     * @see ILookBehavior
     */
    ILookBehavior getLookBehavior();

    /**
     * @return The {@link IFollowProcess} instance
     * @see IFollowProcess
     */
    IFollowProcess getFollowProcess();

    /**
     * @return The {@link IMineProcess} instance
     * @see IMineProcess
     */
    IMineProcess getMineProcess();

    /**
     * @return The {@link IBuilderProcess} instance
     * @see IBuilderProcess
     */
    IBuilderProcess getBuilderProcess();

    /**
     * @return The {@link IExploreProcess} instance
     * @see IExploreProcess
     */
    IExploreProcess getExploreProcess();

    /**
     * @return The {@link IFarmProcess} instance
     * @see IFarmProcess
     */
    IFarmProcess getFarmProcess();

    /**
     * @return The {@link ICustomGoalProcess} instance
     * @see ICustomGoalProcess
     */
    ICustomGoalProcess getCustomGoalProcess();

    /**
     * @return The {@link IGetToBlockProcess} instance
     * @see IGetToBlockProcess
     */
    IGetToBlockProcess getGetToBlockProcess();

    /**
     * @return The {@link IElytraProcess} instance
     * @see IElytraProcess
     */
    IElytraProcess getElytraProcess();

    /**
     * @return The {@link IWorldProvider} instance
     * @see IWorldProvider
     */
    IWorldProvider getWorldProvider();

    /**
     * Returns the {@link IPathingControlManager} for this {@link IBuildDynasty} instance, which is responsible
     * for managing the {@link IBuildDynastyProcess}es which control the {@link IPathingBehavior} state.
     *
     * @return The {@link IPathingControlManager} instance
     * @see IPathingControlManager
     */
    IPathingControlManager getPathingControlManager();

    /**
     * @return The {@link IInputOverrideHandler} instance
     * @see IInputOverrideHandler
     */
    IInputOverrideHandler getInputOverrideHandler();

    /**
     * @return The {@link IPlayerContext} instance
     * @see IPlayerContext
     */
    IPlayerContext getPlayerContext();

    /**
     * @return The {@link IEventBus} instance
     * @see IEventBus
     */
    IEventBus getGameEventHandler();

    /**
     * @return The {@link ISelectionManager} instance
     * @see ISelectionManager
     */
    ISelectionManager getSelectionManager();

    /**
     * @return The {@link ICommandManager} instance
     * @see ICommandManager
     */
    ICommandManager getCommandManager();

    /**
     * Open click
     */
    void openClick();
}
