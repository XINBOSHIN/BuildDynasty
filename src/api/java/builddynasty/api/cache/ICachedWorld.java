
package builddynasty.api.cache;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;


public interface ICachedWorld {


    ICachedRegion getRegion(int regionX, int regionZ);


    void queueForPacking(Chunk chunk);


    boolean isCached(int blockX, int blockZ);


    ArrayList<BlockPos> getLocationsOf(String block, int maximum, int centerX, int centerZ, int maxRegionDistanceSq);


    void reloadAllFromDisk();


    void save();
}
