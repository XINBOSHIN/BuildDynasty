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

package BuildDynasty.api.utils;

import BuildDynasty.api.BuildDynastyAPI;
import net.minecraft.client.entity.EntityPlayerSP;
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
 * @author XINBOSHIN
 * @since 12/14/2018
 */
public interface IPlayerController {

    void syncHeldItem();

    boolean hasBrokenBlock();

    boolean onPlayerDamageBlock(BlockPos pos, EnumFacing side);

    void resetBlockRemoving();

    ItemStack windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player);

    GameType getGameType();

    EnumActionResult processRightClickBlock(EntityPlayerSP player, World world, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand);

    EnumActionResult processRightClick(EntityPlayerSP player, World world, EnumHand hand);

    boolean clickBlock(BlockPos loc, EnumFacing face);

    void setHittingBlock(boolean hittingBlock);

    default double getBlockReachDistance() {
        return this.getGameType().isCreative() ? 5.0F : BuildDynastyAPI.getSettings().blockReachDistance.value;
    }
}
