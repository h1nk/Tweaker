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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import static net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class RenderUtils {
//	public static EntityRenderer getDefaultEntityRenderer() {
//		return Minecraft.getMinecraft().entityRenderer; // FIXME
//	}

	public static void drawCustomSelectionBox(DrawBlockHighlightEvent event) {
//		drawDefaultSelectionBox(event.player, event.target, event.subID, event.partialTicks); // TODO
//		event.context.drawSelectionBox(event.player, event.context.mc.objectMouseOver, 0, event.partialTicks);
	}

	public static void drawCorrectedSelectionBox(DrawBlockHighlightEvent event) {
		drawCustomSelectionBox(event.player, event.target, event.subID, event.partialTicks, 0.0f, 1.0f);
	}

	public static void drawIncorrectSelectionBox(DrawBlockHighlightEvent event) {
		MovingObjectPosition latentPosition = latentYawRayTrace(event.player, 5.0, event.partialTicks);

		drawCustomSelectionBox(event.player, latentPosition, event.subID, event.partialTicks, 1.0f, 0.0f);
	}

	/**
	 * Draws the selection box for the player. Args: entityPlayer, rayTraceHit, i, itemStack, partialTickTime
	 *
	 * @param execute If equals to 0 the method is executed
	 */
	public static void drawCustomizedSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks) {
		// TODO
	}

	/**
	 *
	 * @param entity
	 * @param blockReachDistance
	 * @param partialTicks
	 * @return
	 */
	public static MovingObjectPosition latentYawRayTrace(EntityPlayer entity, double blockReachDistance, float partialTicks) {
		Vec3 vec31;

		Vec3 vec3 = entity.getPositionEyes(partialTicks);

		// Vec3 vec31 = this.getLook(partialTicks);
		if (partialTicks == 1.0F) {
			vec31 = entity.getVectorForRotation(entity.rotationPitch, entity.rotationYawHead);
		} else {
			float f = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			float f1 = entity.prevRotationYawHead + (entity.rotationYawHead - entity.prevRotationYawHead) * partialTicks;
			vec31 = entity.getVectorForRotation(f, f1);
		}

		Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);

		return entity.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	/**
	 * Draws the selection box for the player. Args: entityPlayer, rayTraceHit, i, itemStack, partialTickTime
	 *
	 * @param execute If equals to 0 the method is executed
	 */
//	public static void drawDefaultSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks) {
	private static void drawDefaultSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks) {
		if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
			GL11.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			float f = 0.002F; // FIXME
			BlockPos blockpos = movingObjectPositionIn.getBlockPos();
//			Block block = this.theWorld.getBlockState(blockpos).getBlock();
			Block block = player.worldObj.getBlockState(blockpos).getBlock();

//			if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos))
			if (block.getMaterial() != Material.air && player.worldObj.getWorldBorder().contains(blockpos))
			{
//				block.setBlockBoundsBasedOnState(this.theWorld, blockpos);
				block.setBlockBoundsBasedOnState(player.worldObj, blockpos);
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
//				drawSelectionBoundingBox(block.getSelectedBoundingBox(this.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
				drawSelectionBoundingBox(block.getSelectedBoundingBox(player.worldObj, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}

	private static void drawCustomSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks, float red, float green) {
		if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(red, green, 0.0F, 0.7F);
			GL11.glLineWidth(5.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			BlockPos blockpos = movingObjectPositionIn.getBlockPos();
			Block block = player.worldObj.getBlockState(blockpos).getBlock();

			if (block.getMaterial() != Material.air && player.worldObj.getWorldBorder().contains(blockpos)) {
				block.setBlockBoundsBasedOnState(player.worldObj, blockpos);
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
				drawSelectionBoundingBox(block.getSelectedBoundingBox(player.worldObj, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}
}
