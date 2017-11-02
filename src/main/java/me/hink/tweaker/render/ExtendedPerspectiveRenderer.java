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

package me.hink.tweaker.render;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.*;

import java.util.concurrent.Callable;

import me.hink.tweaker.config.TSettings;

public class ExtendedPerspectiveRenderer extends EntityRenderer {
	public ExtendedPerspectiveRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
		super(mcIn, resourceManagerIn);
	}

	@Override
	public void orientCamera(float partialTicks) {
//		super.orientCamera(partialTicks);

		Entity entity = this.mc.getRenderViewEntity();
		float f = entity.getEyeHeight();
		double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
		double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
		double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;

		if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping())
		{
			f = (float)((double)f + 1.0D);
			GlStateManager.translate(0.0F, 0.3F, 0.0F);

			if (!this.mc.gameSettings.debugCamEnable)
			{
				BlockPos blockpos = new BlockPos(entity);
				IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
				net.minecraftforge.client.ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockpos, iblockstate, entity);

				GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
				GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
			}
		}
		else if (this.mc.gameSettings.thirdPersonView > 0)
		{
			double d3 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);

			if (this.mc.gameSettings.debugCamEnable)
			{
				GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
			}
			else
			{
				float f1 = entity.rotationYaw;
				float f2 = entity.rotationPitch;

				/*
				 * TODO
				 */
				if (TSettings.isPanPerspective)
				{
					f1 = TSettings.cameraYaw;
					f2 = TSettings.cameraPitch;
				}

				if (this.mc.gameSettings.thirdPersonView == 2)
				{
					f2 += 180.0F;
				}

				double d4 = (double)(-MathHelper.sin(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d3;
				double d5 = (double)(MathHelper.cos(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d3;
				double d6 = (double)(-MathHelper.sin(f2 / 180.0F * (float)Math.PI)) * d3;

				for (int i = 0; i < 8; ++i)
				{
					float f3 = (float)((i & 1) * 2 - 1);
					float f4 = (float)((i >> 1 & 1) * 2 - 1);
					float f5 = (float)((i >> 2 & 1) * 2 - 1);
					f3 = f3 * 0.1F;
					f4 = f4 * 0.1F;
					f5 = f5 * 0.1F;
					MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + (double)f3, d1 + (double)f4, d2 + (double)f5), new Vec3(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5));

					if (movingobjectposition != null)
					{
						double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));

						if (d7 < d3)
						{
							d3 = d7;
						}
					}
				}

				if (this.mc.gameSettings.thirdPersonView == 2)
				{
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				/*
				 * TODO
				 */
				if (TSettings.isPanPerspective)
				{
					GlStateManager.rotate(TSettings.cameraPitch - f2, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(TSettings.cameraYaw - f1, 0.0F, 1.0F, 0.0F);
					GlStateManager.translate(0.0F, 0.0F, (float)-d3);
					GlStateManager.rotate(f1 - TSettings.cameraYaw, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(f2 - TSettings.cameraPitch, 1.0F, 0.0F, 0.0F);
				}
				else {
					GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
					GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
					GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
				}
			}
		}
		else
		{
			GlStateManager.translate(0.0F, 0.0F, -0.1F);
		}

		if (!this.mc.gameSettings.debugCamEnable)
		{
			float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
			float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			float roll = 0.0F;
			if (entity instanceof EntityAnimal)
			{
				EntityAnimal entityanimal = (EntityAnimal)entity;
				yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
			}
			Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
			net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup event = new net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup(this, entity, block, partialTicks, yaw, pitch, roll);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

			// TODO
			if (TSettings.isPanPerspective)
			{
				GlStateManager.rotate(event.roll, 0.0F, 0.0F, 1.0F); // TODO: add camera roll keybinds
				GlStateManager.rotate(TSettings.cameraPitch, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(TSettings.cameraYaw + 180.0F, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				GlStateManager.rotate(event.roll, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(event.pitch, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(event.yaw, 0.0F, 1.0F, 0.0F);
			}
		}

		GlStateManager.translate(0.0F, -f, 0.0F);
		d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
		d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
		d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
		this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
	}

	@Override
	public void updateCameraAndRender(float partialTicks, long nanoTime) {
//		super.updateCameraAndRender(partialTicks, nanoTime);

		// TODO
		if ((TSettings.isPanPerspective) && (this.mc.gameSettings.thirdPersonView != 1))
		{
			TSettings.isPanPerspective = false;
		}


		boolean flag = Display.isActive();

		if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1)))
		{
			if (Minecraft.getSystemTime() - this.prevFrameTime > 500L)
			{
				this.mc.displayInGameMenu();
			}
		}
		else
		{
			this.prevFrameTime = Minecraft.getSystemTime();
		}

		this.mc.mcProfiler.startSection("mouse");

		if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow())
		{
			Mouse.setGrabbed(false);
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			Mouse.setGrabbed(true);
		}

		if (this.mc.inGameHasFocus && flag)
		{
			// TODO
			if ((TSettings.isPanPerspective) && (this.mc.gameSettings.thirdPersonView != 1))
			{
				TSettings.isPanPerspective = false;
			}

			this.mc.mouseHelper.mouseXYChange();
			float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f1 = f * f * f * 8.0F;
			float f2 = (float)this.mc.mouseHelper.deltaX * f1;
			float f3 = (float)this.mc.mouseHelper.deltaY * f1;
			int i = 1;

			if (this.mc.gameSettings.invertMouse)
			{
				i = -1;
			}

			if (this.mc.gameSettings.smoothCamera)
			{
				this.smoothCamYaw += f2;
				this.smoothCamPitch += f3;
				float f4 = partialTicks - this.smoothCamPartialTicks;
				this.smoothCamPartialTicks = partialTicks;
				f2 = this.smoothCamFilterX * f4;
				f3 = this.smoothCamFilterY * f4;
//				this.mc.thePlayer.setAngles(f2, f3 * (float)i);
			}
			else
			{
				this.smoothCamYaw = 0.0F;
				this.smoothCamPitch = 0.0F;
//				this.mc.thePlayer.setAngles(f2, f3 * (float)i);
			}

			// TODO
			if (TSettings.isPanPerspective)
			{
				TSettings.cameraYaw += f2 / 8.0F;
				TSettings.cameraPitch += f3 / 8.0F;

				if (Math.abs(TSettings.cameraPitch) > 90.0F)
				{
					TSettings.cameraPitch = (TSettings.cameraPitch > 0.0F ? 90.0F : -90.0F);
				}
			}
			else
			{
				this.mc.thePlayer.setAngles(f2, f3 * i);
			}
		}

		this.mc.mcProfiler.endSection();

		if (!this.mc.skipRenderWorld)
		{
			anaglyphEnable = this.mc.gameSettings.anaglyph;
			final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
			int i1 = scaledresolution.getScaledWidth();
			int j1 = scaledresolution.getScaledHeight();
			final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
			final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
			int i2 = this.mc.gameSettings.limitFramerate;

			if (this.mc.theWorld != null)
			{
				this.mc.mcProfiler.startSection("level");
				int j = Math.min(Minecraft.getDebugFPS(), i2);
				j = Math.max(j, 60);
				long k = System.nanoTime() - nanoTime;
				long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
				this.renderWorld(partialTicks, System.nanoTime() + l);

				if (OpenGlHelper.shadersSupported)
				{
					this.mc.renderGlobal.renderEntityOutlineFramebuffer();

					if (this.theShaderGroup != null && this.useShader)
					{
						GlStateManager.matrixMode(5890);
						GlStateManager.pushMatrix();
						GlStateManager.loadIdentity();
						this.theShaderGroup.loadShaderGroup(partialTicks);
						GlStateManager.popMatrix();
					}

					this.mc.getFramebuffer().bindFramebuffer(true);
				}

				this.renderEndNanoTime = System.nanoTime();
				this.mc.mcProfiler.endStartSection("gui");

				if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null)
				{
					GlStateManager.alphaFunc(516, 0.1F);
					this.mc.ingameGUI.renderGameOverlay(partialTicks);
				}

				this.mc.mcProfiler.endSection();
			}
			else
			{
				GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				GlStateManager.matrixMode(5889);
				GlStateManager.loadIdentity();
				GlStateManager.matrixMode(5888);
				GlStateManager.loadIdentity();
				this.setupOverlayRendering();
				this.renderEndNanoTime = System.nanoTime();
			}

			if (this.mc.currentScreen != null)
			{
				GlStateManager.clear(256);

				try
				{
					net.minecraftforge.client.ForgeHooksClient.drawScreen(this.mc.currentScreen, k1, l1, partialTicks);
				}
				catch (Throwable throwable)
				{
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
					crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>()
					{
						public String call() throws Exception
						{
//							return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
							return Minecraft.getMinecraft().currentScreen.getClass().getCanonicalName();
						}
					});
					crashreportcategory.addCrashSectionCallable("Mouse location", new Callable<String>()
					{
						public String call() throws Exception
						{
							return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] {Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())});
						}
					});
					crashreportcategory.addCrashSectionCallable("Screen size", new Callable<String>()
					{
						public String call() throws Exception
						{
//							return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] {Integer.valueOf(scaledresolution.getScaledWidth()), Integer.valueOf(scaledresolution.getScaledHeight()), Integer.valueOf(EntityRenderer.this.mc.displayWidth), Integer.valueOf(EntityRenderer.this.mc.displayHeight), Integer.valueOf(scaledresolution.getScaleFactor())});
							return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] {Integer.valueOf(scaledresolution.getScaledWidth()), Integer.valueOf(scaledresolution.getScaledHeight()), Integer.valueOf(Minecraft.getMinecraft().displayWidth), Integer.valueOf(Minecraft.getMinecraft().displayHeight), Integer.valueOf(scaledresolution.getScaleFactor())});
						}
					});
					throw new ReportedException(crashreport);
				}
			}
		}
	}
}
