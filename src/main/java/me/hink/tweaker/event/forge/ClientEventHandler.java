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

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;

import me.hink.tweaker.TweakerMod;
import me.hink.tweaker.config.TConfig;
import me.hink.tweaker.gui.GuiCustomGameOver;
import me.hink.tweaker.gui.inventory.GuiCustomInventory;
import me.hink.tweaker.gui.options.controls.GuiCustomControls;
import me.hink.tweaker.gui.options.sounds.GuiScreenCustomOptionsSounds;
import me.hink.tweaker.util.ChatUtils;
import me.hink.tweaker.util.RenderUtils;
import me.hink.tweaker.util.SoundUtils;

import static me.hink.tweaker.config.TConfig.*;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {
	@SubscribeEvent
	public void onTickClientTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START)) {
			Minecraft mc = Minecraft.getMinecraft();

			if (mc.currentScreen != null
					&& (mc.currentScreen instanceof GuiSleepMP)
					&& (mc.thePlayer.isPlayerSleeping())) {
				GuiSleepMP guiSleepMP = (GuiSleepMP) mc.currentScreen;

				String typedText = guiSleepMP.inputField.getText();

				if (TConfig.isPersistentChatEnabled && StringUtils.isNullOrEmpty(typedText)) {
					mc.displayGuiScreen(new GuiChat(typedText));
				}
			}
		}
	}

	@SubscribeEvent
	public void onConfigChangedConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(TweakerMod.MODID)) {
			TConfig.loadConfig();
		}
	}

	@SubscribeEvent
	public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
		if (TConfig.isDebugBlockHighlightingEnabled) {
			event.setCanceled(true);

			RenderUtils.drawCustomSelectionBox(event);

			EntityPlayer player = event.player;

			RenderUtils.drawDebugSelectionBox(event);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onClientChatReceived(ClientChatReceivedEvent event) {
		// TODO: Fix this to actually work
		if (!Minecraft.getMinecraft().gameSettings.chatColours) {
			event.message = ChatUtils.stripColorCodes(event.message);
		}
	}

	@SubscribeEvent()
	public void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event) {
		TweakerMod.LOGGER.info("Player is attempting to sleep in a bed."); // TODO
	}

	@SubscribeEvent
	public void onGuiOpenEvent(GuiOpenEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		if (TConfig.isGuiLoggingEnabled && event.gui != null) {
			TweakerMod.LOGGER.info("Opening GUI " + event.gui.getClass().getSimpleName());
		}

		if (event.gui != null) {
			if (isSearchableControlOptionsGUIEnabled && event.gui instanceof GuiControls) {
				event.gui = new GuiCustomControls(null, Minecraft.getMinecraft().gameSettings);
			} else if (isEnhancedSoundOptionsEnabled && event.gui instanceof GuiScreenOptionsSounds) {
				event.gui = new GuiScreenCustomOptionsSounds(null, Minecraft.getMinecraft().gameSettings);
			} else if (isAutomaticRespawnScreen && event.gui instanceof GuiGameOver) {
				if (mc.thePlayer != null) {
					event.setCanceled(true);

					mc.thePlayer.respawnPlayer();
				}
			} else if (!isRespawnButtonCooldownEnabled && event.gui instanceof GuiGameOver) {
				event.gui = new GuiCustomGameOver();
			} else if (!isPotionEffectRepositioningEnabled && event.gui instanceof GuiInventory) {
				event.gui = new GuiCustomInventory(mc.thePlayer);
			}
		}
	}

	@SubscribeEvent
	public void onPlaySoundEvent(PlaySoundEvent event) {
		if (!isEnhancedSoundOptionsEnabled || event.sound instanceof ITickableSound || Minecraft.getMinecraft().theWorld == null) {
			return;
		}

		float modifier = TweakerMod.soundSettings.getSoundLevel(event.name);

		if (modifier < 1.0f) {
			event.result = SoundUtils.scaleSoundVolume(event.sound, modifier);
		}
	}
}
