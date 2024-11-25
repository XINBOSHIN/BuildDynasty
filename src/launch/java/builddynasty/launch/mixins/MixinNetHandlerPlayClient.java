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

package BuildDynasty.launch.mixins;

import BuildDynasty.api.BuildDynastyAPI;
import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.event.events.BlockChangeEvent;
import BuildDynasty.api.event.events.ChunkEvent;
import BuildDynasty.api.event.events.type.EventState;
import BuildDynasty.api.utils.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author XINBOSHIN
 * @since 8/3/2018
 */
@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Inject(
            method = "handleChunkData",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/world/chunk/Chunk.read(Lnet/minecraft/network/PacketBuffer;IZ)V"
            )
    )
    private void preRead(SPacketChunkData packetIn, CallbackInfo ci) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForConnection((NetHandlerPlayClient) (Object) this);
        if (BuildDynasty == null) {
            return;
        }
        BuildDynasty.getGameEventHandler().onChunkEvent(
                new ChunkEvent(
                        EventState.PRE,
                        packetIn.isFullChunk() ? ChunkEvent.Type.POPULATE_FULL : ChunkEvent.Type.POPULATE_PARTIAL,
                        packetIn.getChunkX(),
                        packetIn.getChunkZ()
                )
        );
    }

    @Inject(
            method = "handleChunkData",
            at = @At("RETURN")
    )
    private void postHandleChunkData(SPacketChunkData packetIn, CallbackInfo ci) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForConnection((NetHandlerPlayClient) (Object) this);
        if (BuildDynasty == null) {
            return;
        }
        BuildDynasty.getGameEventHandler().onChunkEvent(
                new ChunkEvent(
                        EventState.POST,
                        packetIn.isFullChunk() ? ChunkEvent.Type.POPULATE_FULL : ChunkEvent.Type.POPULATE_PARTIAL,
                        packetIn.getChunkX(),
                        packetIn.getChunkZ()
                )
        );
    }

    @Inject(
            method = "handleBlockChange",
            at = @At("RETURN")
    )
    private void postHandleBlockChange(SPacketBlockChange packetIn, CallbackInfo ci) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForConnection((NetHandlerPlayClient) (Object) this);
        if (BuildDynasty == null) {
            return;
        }

        final ChunkPos pos = new ChunkPos(packetIn.getBlockPosition().getX() >> 4, packetIn.getBlockPosition().getZ() >> 4);
        final Pair<BlockPos, IBlockState> changed = new Pair<>(packetIn.getBlockPosition(), packetIn.getBlockState());
        BuildDynasty.getGameEventHandler().onBlockChange(new BlockChangeEvent(pos, Collections.singletonList(changed)));
    }

    @Inject(
            method = "handleMultiBlockChange",
            at = @At("RETURN")
    )
    private void postHandleMultiBlockChange(SPacketMultiBlockChange packetIn, CallbackInfo ci) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForConnection((NetHandlerPlayClient) (Object) this);
        if (BuildDynasty == null) {
            return;
        }

        // All blocks have the same ChunkPos
        final ChunkPos pos = new ChunkPos(packetIn.getChangedBlocks()[0].getPos());

        BuildDynasty.getGameEventHandler().onBlockChange(new BlockChangeEvent(
                pos,
                Arrays.stream(packetIn.getChangedBlocks())
                        .map(data -> new Pair<>(data.getPos(), data.getBlockState()))
                        .collect(Collectors.toList())
        ));
    }

    @Inject(
            method = "handleCombatEvent",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/Minecraft.displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"
            )
    )
    private void onPlayerDeath(SPacketCombatEvent packetIn, CallbackInfo ci) {
        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getBuildDynastyForConnection((NetHandlerPlayClient) (Object) this);
        if (BuildDynasty == null) {
            return;
        }
        BuildDynasty.getGameEventHandler().onPlayerDeath();
    }
}
