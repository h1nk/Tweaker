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

package me.hink.tweaker.event.forge;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.util.ChatComponentText;

import me.hink.tweaker.config.TSettings;
import me.hink.tweaker.util.ScreenShotUtils;

import static me.hink.tweaker.binds.KeyBindings.*;

/**
 * TODO
 *
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
public class KeyInputHandler {
	private static Minecraft mc = Minecraft.getMinecraft();
	private static boolean wasPressed = false;

	private static int initialPerspective = 0;

	private static boolean isMuted = false;

	private static float previousMasterVolume = -0.0F;

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (viewPerspective3.isPressed()) {
			wasPressed = true;

			initialPerspective = mc.gameSettings.thirdPersonView;

			mc.gameSettings.thirdPersonView = 2;
		} else if (!viewPerspective3.isKeyDown() && wasPressed) {
			wasPressed = false;

			mc.gameSettings.thirdPersonView = initialPerspective;
		} else if (mc.gameSettings.keyBindScreenshot.isPressed()) {
			ScreenShotUtils.saveHighResScreenShot();
		}
		if (muteSound.isPressed()) {
			if (previousMasterVolume < 0.0F) {
				previousMasterVolume = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
			}

			if (isMuted) {
				isMuted = false;

				mc.gameSettings.setSoundLevel(SoundCategory.MASTER, previousMasterVolume);
				mc.thePlayer.addChatMessage(new ChatComponentText("Unmuted player sound"));
			} else {
				isMuted = true;

				previousMasterVolume = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);

				mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 0.0F);
				mc.thePlayer.addChatMessage(new ChatComponentText("Muted player sound"));
			}
		}
		if (toggleDebugCamera.isPressed()) {
			Minecraft.getMinecraft().gameSettings.debugCamEnable = !Minecraft.getMinecraft().gameSettings.debugCamEnable;
		}
		if (viewPanPerspective.isPressed()) {
			Minecraft mc = Minecraft.getMinecraft();

			TSettings.isPanPerspective = !TSettings.isPanPerspective;

			TSettings.cameraYaw = mc.thePlayer.cameraYaw;
			TSettings.cameraPitch = mc.thePlayer.cameraPitch;

			if (TSettings.isPanPerspective) {
				mc.gameSettings.thirdPersonView = 1;
			} else {
				mc.gameSettings.thirdPersonView = 0;
			}
		}
	}
}
