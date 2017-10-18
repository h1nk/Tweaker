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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import me.hink.tweaker.util.SoundUtils;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class GuiSlotSoundList extends GuiScrollingList {
	private GuiScreen parent;

	private List<String> soundNames = SoundUtils.getRegisteredSounds();
	public List<Slider> soundSliders = Lists.newArrayList();

	public GuiSlotSoundList(GuiScreen parent) {
		// TODO: resize based on above buttons
		super(parent.mc, 150 * 2 + 10, parent.height, parent.height / 2 - 5, parent.height - 35, (parent.width / 2) - (150 + (10 / 2)), 22, parent.width, parent.height);

		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return soundNames.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {}

	@Override
	protected boolean isSelected(int index) {
		return false;
	}

	@Override
	protected void drawBackground() {}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		String soundName = soundNames.get(slotIdx);

		Slider slider = new Slider(440 + slotIdx, left + 3, slotTop + 3, soundName, true);
		slider.drawButton(Minecraft.getMinecraft(), left + 3, slotTop + 3);
	}

	public List<Slider> getButtons() {
		return this.soundSliders;
	}

	@SideOnly(Side.CLIENT)
	class Slider extends GuiButton {
		private final String soundName;
		public float volume = 1.0F;

		public boolean idk;

		public Slider(int sliderId, int x, int y, String soundName, boolean idk /* TODO */) {
			super(sliderId, x, y, 150, 20, "");

			this.soundName = soundName;
			this.displayString = soundName; // TODO: set string based on localized name
			this.volume = TweakerMod.soundSettings.getSoundLevel(soundName);
		}

		protected int getHoverState(boolean mouseOver) {
			return 0;
		}

		protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
			if (this.visible) {
				if (this.idk) {
					this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
					this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);

					TweakerMod.soundSettings.setSoundLevel(this.soundName, this.volume);
					TweakerMod.soundSettings.saveOptions();

					this.displayString = this.soundName + ": " + TweakerMod.soundSettings.getSoundLevel(this.soundName);
				}

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
				this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
			}
		}

		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			if (super.mousePressed(mc, mouseX, mouseY)) {
				this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
				this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);

				TweakerMod.soundSettings.setSoundLevel(this.soundName, this.volume);
				TweakerMod.soundSettings.saveOptions();

				this.displayString = this.soundName + ": " + TweakerMod.soundSettings.getSoundLevel(this.soundName);
				this.idk = true;

				return true;
			} else {
				return false;
			}
		}

		public void playPressSound(SoundHandler soundHandlerIn) {}

		public void mouseReleased(int mouseX, int mouseY) {
			if (this.idk) {
				TweakerMod.soundSettings.getSoundLevel(this.soundName);

				parent.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}

			this.idk = false;
		}
	}
}