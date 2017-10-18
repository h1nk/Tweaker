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

package me.hink.tweaker.gui.options.sounds;

import com.google.common.collect.Lists;

import me.hink.tweaker.TweakerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import me.hink.tweaker.util.SoundUtils;
import org.lwjgl.Sys;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
@SideOnly(Side.CLIENT)
public class GuiSoundList extends GuiListExtended {
	private final GuiScreenCustomOptionsSounds parentScreen;
	private final Minecraft mc;

	private final GuiListExtended.IGuiListEntry[] soundListEntries;

	public GuiSoundList(GuiScreenCustomOptionsSounds parent, Minecraft minecraft, String filter) {
		// FIXME: top calculation relative
		super(minecraft, parent.width, parent.height, parent.topOfScrollList + 5, parent.height - 45 - 1, 20); // Sadly left is always 0 #BlameNotch

		this.parentScreen = parent;
		this.mc = minecraft;

		List<String> soundNames = SoundUtils.getRegisteredSounds();

		if (filter == null) {
			this.soundListEntries = new GuiListExtended.IGuiListEntry[SoundUtils.getRegisteredSounds().size()];

			for (int i = 0; i < this.soundListEntries.length; i++) {
				this.soundListEntries[i] = new SoundVolumeEntry(soundNames.get(i));
			}

		} else {
			List<String> newSounds = Lists.newArrayList();

			for (String soundName : soundNames) {
				if (soundName.toLowerCase().contains(filter.toLowerCase())) {
					newSounds.add(soundName);
				}
			}

			this.soundListEntries = new GuiListExtended.IGuiListEntry[newSounds.size()];

			for (int i = 0; i < this.soundListEntries.length; i++) {
				this.soundListEntries[i] = new SoundVolumeEntry(newSounds.get(i));
			}
		}
	}

	public IGuiListEntry getListEntry(int index) {
		return this.soundListEntries[index]; // TODO: Can't be null
	}

	protected int getSize() {
		return this.soundListEntries.length;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		return super.mouseClicked(mouseX, mouseY, mouseEvent);
	}

	@SideOnly(Side.CLIENT)
	class Slider extends GuiButton {
		private final String soundName;
		public float volume = 1.0F;

		public boolean isSelected;

		private static final int maxLength = 15;

		private long lastHoverTime = 0;

		public Slider(int sliderId, int x, int y, String soundName) {
			super(sliderId, x, y, 150, 20, "");

			this.soundName = soundName;
			// TODO: set string based on localized name

			this.displayString = (this.soundName.length() > maxLength) ? this.soundName.substring(0, maxLength) + "..." : this.soundName;
			this.displayString += " " + SoundUtils.getSoundVolume(this.soundName);

			this.volume = TweakerMod.soundSettings.getSoundLevel(soundName);
		}

		@Override
		protected int getHoverState(boolean mouseOver) {
			return 0;
		}

		@Override
		protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
			if (this.visible) {
				if (this.isSelected) {
					scrollMultiplier = 0;

					this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
					this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);

					TweakerMod.soundSettings.setSoundLevel(this.soundName, this.volume);
					TweakerMod.soundSettings.saveOptions();

					this.displayString = (this.soundName.length() > maxLength) ? this.soundName.substring(0, maxLength) + "..." : this.soundName;
					this.displayString += " " + SoundUtils.getSoundVolume(this.soundName);
				}

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
				this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
			}
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			if (super.mousePressed(mc, mouseX, mouseY)) {
				// New volume based on slider
				this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
				this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);

				// Set and save sound level
				TweakerMod.soundSettings.setSoundLevel(this.soundName, this.volume);
				TweakerMod.soundSettings.saveOptions();

				// Update display text based on new sound volume level
				this.displayString = this.soundName + ": " + TweakerMod.soundSettings.getSoundLevel(this.soundName);
				this.isSelected = true;

				return true;
			} else {
				return false;
			}
		}

		public void playPressSound(SoundHandler soundHandlerIn) {}

		public void mouseReleased(int mouseX, int mouseY) {
			if (this.isSelected) {
//				if (this.soundName == "") {
//					float f = 1.0F;
//				} else {
//					GuiScreenCustomOptionsSounds.this.gameSettings.getSoundLevel(this.soundCategory);
//				}

				Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}

			this.isSelected = false;
		}
	}

	@SideOnly(Side.CLIENT)
	class GuiButtonPlaySound extends GuiButton {
		public GuiButtonPlaySound(int buttonID, int x, int y) {
			super(buttonID, x, y, 20, 20, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			super.drawButton(mc, mouseX, mouseY);

			if (this.visible) {
				mc.getTextureManager().bindTexture(new ResourceLocation(TweakerMod.MODID, "textures/gui/widgets.png"));

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				boolean isHover = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

				int i = 20 * 2;

				if (isHover) {
					i += this.height;
				}

				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width, this.height);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public class SoundVolumeEntry implements GuiListExtended.IGuiListEntry {
		private final String soundName;

		private final Slider changeVolumeSlider;
		private final GuiButtonPlaySound btnPlaySound;

		private SoundVolumeEntry(String soundName) {
			this.soundName = soundName;

			this.changeVolumeSlider = new Slider(0, 0, 0, this.soundName);
			this.btnPlaySound = new GuiButtonPlaySound(999, 0, 0);
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
			this.btnPlaySound.xPosition = x + 165;
			this.btnPlaySound.yPosition = y;

			this.changeVolumeSlider.xPosition = x;
			this.changeVolumeSlider.yPosition = y;

			this.changeVolumeSlider.drawButton(mc, mouseX, mouseY);
			this.btnPlaySound.drawButton(mc, mouseX, mouseY);
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int mouseButton, int p_148278_5_, int p_148278_6_) {//
			if (mouseButton == 0 && this.btnPlaySound.mousePressed(Minecraft.getMinecraft(), p_148278_2_, p_148278_3_)) {
				parentScreen.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(this.soundName), 1.0F));
			} else if (mouseButton == 1 && this.btnPlaySound.mousePressed(Minecraft.getMinecraft(), p_148278_2_, p_148278_3_)) {
				if (parentScreen.mc.getSoundHandler().isSoundPlaying(PositionedSoundRecord.create(new ResourceLocation(this.soundName), 1.0F))) {
					parentScreen.mc.getSoundHandler().stopSound(PositionedSoundRecord.create(new ResourceLocation(this.soundName), 1.0F));
				}
			}

			this.changeVolumeSlider.mousePressed(Minecraft.getMinecraft(), p_148278_2_, p_148278_3_);

			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			this.changeVolumeSlider.mouseReleased(x, y);
			this.btnPlaySound.mouseReleased(x, y);
		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
	}
}
