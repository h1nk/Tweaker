/*
 * Copyright (c) 2017 h1nk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.hink.tweaker.util;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class RenderUtils {
	public static EntityRenderer getDefaultEntityRenderer() {
		return Minecraft.getMinecraft().entityRenderer;
	}

	public Vec3 getIncorrectLookVector(float partialTicks) {
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;

		if (thePlayer != null) {
			if (partialTicks == 1.0F) {
				return thePlayer.getVectorForRotation(thePlayer.rotationPitch, thePlayer.rotationYaw);
			} else {
				float f1 = thePlayer.prevRotationPitch + (thePlayer.rotationPitch - thePlayer.prevRotationPitch) * partialTicks;
				float f2 = thePlayer.prevRotationYawHead + (thePlayer.rotationYawHead - thePlayer.prevRotationYawHead) * partialTicks;

				return thePlayer.getVectorForRotation(f1, f2);
			}
		} else return null;
	}

	public static void drawCustomSelectionBox(DrawBlockHighlightEvent event) {
//		drawSelectionBox(event.player, event.target, event.subID, event.partialTicks, 2.0f);
	}

	public static void drawDebugSelectionBox(DrawBlockHighlightEvent event) {
		drawSelectionBox(event.player, event.target, event.subID, event.partialTicks, 0.0f);
	}

	public static void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks, float color) {
		Minecraft mc = Minecraft.getMinecraft();

		if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(0.0f, 1.0f, 0.0f, 0.75f);

			GL11.glLineWidth(7.5F);

			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);

			BlockPos blockpos = movingObjectPositionIn.getBlockPos();

			Block block = mc.theWorld.getBlockState(blockpos).getBlock();

			if (block.getMaterial() != Material.air && mc.theWorld.getWorldBorder().contains(blockpos)) {
				block.setBlockBoundsBasedOnState(mc.theWorld, blockpos);

				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;

				drawSelectionBoundingBox(block.getSelectedBoundingBox(mc.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}

	private static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		tessellator.draw();
	}
}
