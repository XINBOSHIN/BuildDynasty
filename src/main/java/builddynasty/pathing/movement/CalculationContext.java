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

package BuildDynasty.pathing.movement;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.pathing.movement.ActionCosts;
import BuildDynasty.cache.WorldData;
import BuildDynasty.pathing.precompute.PrecomputedData;
import BuildDynasty.utils.BlockStateInterface;
import BuildDynasty.utils.ToolSet;
import BuildDynasty.utils.pathing.BetterWorldBorder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static BuildDynasty.api.pathing.movement.ActionCosts.COST_INF;

/**
 * @author XINBOSHIN
 * @since 8/7/2018
 */
public class CalculationContext {

    private static final ItemStack STACK_BUCKET_WATER = new ItemStack(Items.WATER_BUCKET);

    public final boolean safeForThreadedUse;
    public final IBuildDynasty BuildDynasty;
    public final World world;
    public final WorldData worldData;
    public final BlockStateInterface bsi;
    public final ToolSet toolSet;
    public final boolean hasWaterBucket;
    public final boolean hasThrowaway;
    public final boolean canSprint;
    protected final double placeBlockCost; // protected because you should call the function instead
    public final boolean allowBreak;
    public final List<Block> allowBreakAnyway;
    public final boolean allowParkour;
    public final boolean allowParkourPlace;
    public final boolean allowJumpAt256;
    public final boolean allowParkourAscend;
    public final boolean assumeWalkOnWater;
    public boolean allowFallIntoLava;
    public final int frostWalker;
    public final boolean allowDiagonalDescend;
    public final boolean allowDiagonalAscend;
    public final boolean allowDownward;
    public int minFallHeight;
    public int maxFallHeightNoWater;
    public final int maxFallHeightBucket;
    public final double waterWalkSpeed;
    public final double breakBlockAdditionalCost;
    public double backtrackCostFavoringCoefficient;
    public double jumpPenalty;
    public final double walkOnWaterOnePenalty;
    public final BetterWorldBorder worldBorder;

    public final PrecomputedData precomputedData;

    public CalculationContext(IBuildDynasty BuildDynasty) {
        this(BuildDynasty, false);
    }

    public CalculationContext(IBuildDynasty BuildDynasty, boolean forUseOnAnotherThread) {
        this.precomputedData = new PrecomputedData();
        this.safeForThreadedUse = forUseOnAnotherThread;
        this.BuildDynasty = BuildDynasty;
        EntityPlayerSP player = BuildDynasty.getPlayerContext().player();
        this.world = BuildDynasty.getPlayerContext().world();
        this.worldData = (WorldData) BuildDynasty.getPlayerContext().worldData();
        this.bsi = new BlockStateInterface(BuildDynasty.getPlayerContext(), forUseOnAnotherThread);
        this.toolSet = new ToolSet(player);
        this.hasThrowaway = BuildDynasty.settings().allowPlace.value && ((BuildDynasty) BuildDynasty).getInventoryBehavior().hasGenericThrowaway();
        this.hasWaterBucket = BuildDynasty.settings().allowWaterBucketFall.value && InventoryPlayer.isHotbar(player.inventory.getSlotFor(STACK_BUCKET_WATER)) && !world.provider.isNether();
        this.canSprint = BuildDynasty.settings().allowSprint.value && player.getFoodStats().getFoodLevel() > 6;
        this.placeBlockCost = BuildDynasty.settings().blockPlacementPenalty.value;
        this.allowBreak = BuildDynasty.settings().allowBreak.value;
        this.allowBreakAnyway = new ArrayList<>(BuildDynasty.settings().allowBreakAnyway.value);
        this.allowParkour = BuildDynasty.settings().allowParkour.value;
        this.allowParkourPlace = BuildDynasty.settings().allowParkourPlace.value;
        this.allowJumpAt256 = BuildDynasty.settings().allowJumpAt256.value;
        this.allowParkourAscend = BuildDynasty.settings().allowParkourAscend.value;
        this.assumeWalkOnWater = BuildDynasty.settings().assumeWalkOnWater.value;
        this.allowFallIntoLava = false; // Super secret internal setting for ElytraBehavior
        this.frostWalker = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, BuildDynasty.getPlayerContext().player());
        this.allowDiagonalDescend = BuildDynasty.settings().allowDiagonalDescend.value;
        this.allowDiagonalAscend = BuildDynasty.settings().allowDiagonalAscend.value;
        this.allowDownward = BuildDynasty.settings().allowDownward.value;
        this.minFallHeight = 3; // Minimum fall height used by MovementFall
        this.maxFallHeightNoWater = BuildDynasty.settings().maxFallHeightNoWater.value;
        this.maxFallHeightBucket = BuildDynasty.settings().maxFallHeightBucket.value;
        int depth = EnchantmentHelper.getDepthStriderModifier(player);
        if (depth > 3) {
            depth = 3;
        }
        float mult = depth / 3.0F;
        this.waterWalkSpeed = ActionCosts.WALK_ONE_IN_WATER_COST * (1 - mult) + ActionCosts.WALK_ONE_BLOCK_COST * mult;
        this.breakBlockAdditionalCost = BuildDynasty.settings().blockBreakAdditionalPenalty.value;
        this.backtrackCostFavoringCoefficient = BuildDynasty.settings().backtrackCostFavoringCoefficient.value;
        this.jumpPenalty = BuildDynasty.settings().jumpPenalty.value;
        this.walkOnWaterOnePenalty = BuildDynasty.settings().walkOnWaterOnePenalty.value;
        // why cache these things here, why not let the movements just get directly from settings?
        // because if some movements are calculated one way and others are calculated another way,
        // then you get a wildly inconsistent path that isn't optimal for either scenario.
        this.worldBorder = new BetterWorldBorder(world.getWorldBorder());
    }

    public final IBuildDynasty getBuildDynasty() {
        return BuildDynasty;
    }

    public IBlockState get(int x, int y, int z) {
        return bsi.get0(x, y, z); // laughs maniacally
    }

    public boolean isLoaded(int x, int z) {
        return bsi.isLoaded(x, z);
    }

    public IBlockState get(BlockPos pos) {
        return get(pos.getX(), pos.getY(), pos.getZ());
    }

    public Block getBlock(int x, int y, int z) {
        return get(x, y, z).getBlock();
    }

    public double costOfPlacingAt(int x, int y, int z, IBlockState current) {
        if (!hasThrowaway) { // only true if allowPlace is true, see constructor
            return COST_INF;
        }
        if (isPossiblyProtected(x, y, z)) {
            return COST_INF;
        }
        if (!worldBorder.canPlaceAt(x, z)) {
            return COST_INF;
        }
        return placeBlockCost;
    }

    public double breakCostMultiplierAt(int x, int y, int z, IBlockState current) {
        if (!allowBreak && !allowBreakAnyway.contains(current.getBlock())) {
            return COST_INF;
        }
        if (isPossiblyProtected(x, y, z)) {
            return COST_INF;
        }
        return 1;
    }

    public double placeBucketCost() {
        return placeBlockCost; // shrug
    }

    public boolean isPossiblyProtected(int x, int y, int z) {
        // TODO more protection logic here; see #220
        return false;
    }
}
