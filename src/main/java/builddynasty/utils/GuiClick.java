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

package BuildDynasty.utils;

import BuildDynasty.BuildDynasty;
import BuildDynasty.api.BuildDynastyAPI;
import BuildDynasty.api.pathing.goals.GoalBlock;
import BuildDynasty.api.utils.BetterBlockPos;
import BuildDynasty.api.utils.Helper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collections;

import static BuildDynasty.api.command.IBuildDynastyChatControl.FORCE_COMMAND_PREFIX;
import static org.lwjgl.opengl.GL11.*;

public class GuiClick extends GuiScreen {

    // My name is XINBOSHIN and I grant leijurv permission to use this pasted code
    private final FloatBuffer MODELVIEW = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer PROJECTION = BufferUtils.createFloatBuffer(16);
    private final IntBuffer VIEWPORT = BufferUtils.createIntBuffer(16);
    private final FloatBuffer TO_WORLD_BUFFER = BufferUtils.createFloatBuffer(3);

    private BlockPos clickStart;
    private BlockPos currentMouseOver;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int mx = Mouse.getX();
        int my = Mouse.getY();
        Vec3d near = toWorld(mx, my, 0);
        Vec3d far = toWorld(mx, my, 1); // "Use 0.945 that's what stack overflow says" - leijurv
        if (near != null && far != null) {
            Vec3d viewerPos = new Vec3d(mc.getRenderManager().viewerPosX, mc.getRenderManager().viewerPosY, mc.getRenderManager().viewerPosZ);
            RayTraceResult result = mc.world.rayTraceBlocks(near.add(viewerPos), far.add(viewerPos), false, false, true);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                currentMouseOver = result.getBlockPos();
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (currentMouseOver != null) { //Catch this, or else a click into void will result in a crash
            if (mouseButton == 0) {
                if (clickStart != null && !clickStart.equals(currentMouseOver)) {
                    BuildDynastyAPI.getProvider().getPrimaryBuildDynasty().getSelectionManager().removeAllSelections();
                    BuildDynastyAPI.getProvider().getPrimaryBuildDynasty().getSelectionManager().addSelection(BetterBlockPos.from(clickStart), BetterBlockPos.from(currentMouseOver));
                    ITextComponent component = new TextComponentString("Selection made! For usage: " + BuildDynasty.settings().prefix.value + "help sel");
                    component.getStyle()
                            .setColor(TextFormatting.WHITE)
                            .setClickEvent(new ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    FORCE_COMMAND_PREFIX + "help sel"
                            ));
                    Helper.HELPER.logDirect(component);
                    clickStart = null;
                } else {
                    BuildDynastyAPI.getProvider().getPrimaryBuildDynasty().getCustomGoalProcess().setGoalAndPath(new GoalBlock(currentMouseOver));
                }
            } else if (mouseButton == 1) {
                BuildDynastyAPI.getProvider().getPrimaryBuildDynasty().getCustomGoalProcess().setGoalAndPath(new GoalBlock(currentMouseOver.up()));
            }
        }
        clickStart = null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        clickStart = currentMouseOver;
    }

    public void onRender() {
        GlStateManager.getFloat(GL_MODELVIEW_MATRIX, (FloatBuffer) MODELVIEW.clear());
        GlStateManager.getFloat(GL_PROJECTION_MATRIX, (FloatBuffer) PROJECTION.clear());
        GlStateManager.glGetInteger(GL_VIEWPORT, (IntBuffer) VIEWPORT.clear());

        if (currentMouseOver != null) {
            Entity e = mc.getRenderViewEntity();
            // drawSingleSelectionBox WHEN?
            PathRenderer.drawManySelectionBoxes(e, Collections.singletonList(currentMouseOver), Color.CYAN);
            if (clickStart != null && !clickStart.equals(currentMouseOver)) {
                IRenderer.startLines(Color.RED, BuildDynasty.settings().pathRenderLineWidthPixels.value, true);
                BetterBlockPos a = new BetterBlockPos(currentMouseOver);
                BetterBlockPos b = new BetterBlockPos(clickStart);
                IRenderer.emitAABB(new AxisAlignedBB(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z), Math.max(a.x, b.x) + 1, Math.max(a.y, b.y) + 1, Math.max(a.z, b.z) + 1));
                IRenderer.endLines(true);
            }
        }
    }

    private Vec3d toWorld(double x, double y, double z) {
        boolean result = GLU.gluUnProject((float) x, (float) y, (float) z, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer) TO_WORLD_BUFFER.clear());
        if (result) {
            return new Vec3d(TO_WORLD_BUFFER.get(0), TO_WORLD_BUFFER.get(1), TO_WORLD_BUFFER.get(2));
        }
        return null;
    }
}
