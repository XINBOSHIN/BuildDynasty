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

package BuildDynasty.utils.player;

import BuildDynasty.api.utils.IPlayerController;
import BuildDynasty.utils.accessor.IPlayerControllerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

/**
 * Implementation of {@link IPlayerController} that chains to the primary player controller's methods
 *
 * @author XINBOSHIN
 * @since 12/14/2018
 */
public final class BuildDynastyPlayerController implements IPlayerController {

    private final Minecraft mc;

    public BuildDynastyPlayerController(Minecraft mc) {
        this.mc = mc;
    }

    @Override
    public void syncHeldItem() {
        ((IPlayerControllerMP) mc.playerController).callSyncCurrentPlayItem();
    }

    @Override
    public boolean hasBrokenBlock() {
        return ((IPlayerControllerMP) mc.playerController).getCurrentBlock().getY() == -1;
    }

    @Override
    public boolean onPlayerDamageBlock(BlockPos pos, EnumFacing side) {
        return mc.playerController.onPlayerDamageBlock(pos, side);
    }

    @Override
    public void resetBlockRemoving() {
        mc.playerController.resetBlockRemoving();
    }

    @Override
    public ItemStack windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player) {
        return mc.playerController.windowClick(windowId, slotId, mouseButton, type, player);
    }

    @Override
    public GameType getGameType() {
        return mc.playerController.getCurrentGameType();
    }

    @Override
    public EnumActionResult processRightClickBlock(EntityPlayerSP player, World world, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand) {
        return mc.playerController.processRightClickBlock(player, (WorldClient) world, pos, direction, vec, hand);
    }

    @Override
    public EnumActionResult processRightClick(EntityPlayerSP player, World world, EnumHand hand) {
        return mc.playerController.processRightClick(player, world, hand);
    }

    @Override
    public boolean clickBlock(BlockPos loc, EnumFacing face) {
        return mc.playerController.clickBlock(loc, face);
    }

    @Override
    public void setHittingBlock(boolean hittingBlock) {
        ((IPlayerControllerMP) mc.playerController).setIsHittingBlock(hittingBlock);
    }
}