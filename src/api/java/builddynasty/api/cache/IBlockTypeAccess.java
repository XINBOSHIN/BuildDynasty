
package builddynasty.api.cache;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IBlockTypeAccess {

    IBlockState getBlock(int x, int y, int z);

    default IBlockState getBlock(BlockPos pos) {
        return getBlock(pos.getX(), pos.getY(), pos.getZ());
    }
}
