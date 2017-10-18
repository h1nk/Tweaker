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

import me.hink.tweaker.TweakerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Random;

import me.hink.tweaker.config.SoundSettings;
import me.hink.tweaker.util.SoundUtils;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
@SideOnly(Side.CLIENT)
public class GuiScreenCustomOptionsSounds extends GuiScreen {
	private final GuiScreen parentGuiScreen;

	private final GameSettings gameSettings;
	private final SoundSettings soundSettings;

	protected String optionsText = "Options";

	private String sliderOffText;

	private GuiSoundList soundVolumeList;

	private GuiTextField search;
	public String searchText = null;

	private GuiCheckBox checkBox;

	protected int topOfScrollList = height / 2 - 5;

	public GuiScreenCustomOptionsSounds(GuiScreen parentGuiScreen, GameSettings gameSettings) {
		this.parentGuiScreen = parentGuiScreen;
		this.gameSettings = gameSettings;
		this.soundSettings = TweakerMod.soundSettings;
	}

	public void initGui() {
		this.optionsText = I18n.format("options.sounds.title", new Object[0]);
		this.sliderOffText = I18n.format("options.off", new Object[0]);

		byte b = 0;

		// Master sound volume slider
		this.buttonList.add(new Slider(SoundCategory.MASTER.getCategoryId(), this.width / 2 - 155 + b % 2 * 160, 20, SoundCategory.MASTER, true));

		// Sound category sliders
		SoundCategory[] aSoundCategory = SoundCategory.values();

		int i = aSoundCategory.length;
		int k = 2;

		for (int j = 0; j < i; ++j) {
			SoundCategory soundcategory = aSoundCategory[j];

			if (soundcategory != SoundCategory.MASTER) {
				this.buttonList.add(new Slider(soundcategory.getCategoryId(), this.width / 2 - 155 + k % 2 * 160, 25 + 24 * (k >> 1), soundcategory, false));
				++k;

				if (k == 10) {
					this.topOfScrollList = 25 + 24 * (k >> 1);
				}
			}
		}

		// Sound volume list
		this.soundVolumeList = new GuiSoundList(this, this.mc, this.searchText);
//		this.soundsList = new GuiSlotSoundList(this);

		// Only Show Currently Playing
//		onlyShowCurrentlyPlaying = new GuiCheckBox(101, this.width / 2 - 100, 15, "Only Show Currently Playing Sounds", false);

		// Search text field
		search = new GuiTextField(0, this.mc.fontRendererObj, this.width / 2 - 150, this.height - 20, 145, 15);
		search.setFocused(false);
		search.setCanLoseFocus(true);

		// Show currently playing
		checkBox = new GuiCheckBox(new Random().nextInt(), search.xPosition, search.yPosition - 20, "Currently playing", false);

		// Bottom Buttons
		this.buttonList.add(new GuiButton(443, this.width / 2 + 6, this.height - 40 - 4, 150, 20, "Reset Sounds"));
		this.buttonList.add(new GuiButton(200, this.width / 2 + 6, this.height - 20 - 3, 150, 20, I18n.format("gui.done", new Object[0])));
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		this.soundVolumeList.handleMouseInput(); // needed for scrolling
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.soundSettings.saveOptions();

				this.mc.displayGuiScreen(this.parentGuiScreen);
			} else if (button.id == 443) {
				this.soundSettings.resetSoundLevels();

				this.soundVolumeList = new GuiSoundList(this, this.mc, null);
			}
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.soundVolumeList.mouseClicked(mouseX, mouseY, mouseButton);

		// All other UI buttons
		super.mouseClicked(mouseX, mouseY, mouseButton);

		// Check box
		this.checkBox.mousePressed(this.mc, mouseX, mouseY);

		// Search text field
		this.search.mouseClicked(mouseX, mouseY, mouseButton);

		// Right clicking within the search field clears like any other Forge mod search field (like the mod info list, JEI or AE2 terminals)
		if (mouseButton == 1 && mouseX >= search.xPosition && mouseX < search.xPosition + search.width && mouseY >= search.yPosition && mouseY < search.yPosition + search.height) {
			search.setText("");
			this.search.setTextColor(0xFFFFFF);

			this.soundVolumeList = new GuiSoundList(this, this.mc, null);
		}
	}



	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (state != 0 || !this.soundVolumeList.mouseReleased(mouseX, mouseY, state)) {
			super.mouseReleased(mouseX, mouseY, state);
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// GUI background
		this.drawDefaultBackground();

		// Sound volume list GUI
		this.soundVolumeList.drawScreen(mouseX, mouseY, partialTicks);

		// Title text
		this.drawCenteredString(this.fontRendererObj, this.optionsText, this.width / 2, 7, 0xFFFFFF);

		// Checkbox
		checkBox.drawButton(this.mc, mouseX, mouseY);

		// Search text field
		search.drawTextBox();

		// Other simple buttons
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);

		// Typed character in search text field
		if (this.search.textboxKeyTyped(typedChar, keyCode)) {
			this.searchText = search.getText();

			if (SoundUtils.isValidSoundName(this.searchText)) {
				this.search.setTextColor(0xFFFFFF);

				this.soundVolumeList = new GuiSoundList(this, this.mc, this.searchText);
			} else {
				this.search.setTextColor(0xFF0000);

				this.soundVolumeList = new GuiSoundList(this, this.mc, "XXXXXXXXXX");
			}
		}
	}

	@Override
	public void updateScreen() {
		// nothing
		super.updateScreen();

		// Update screen for text search cursor
		this.search.updateCursorCounter();
	}

	// Get sound category volume as percentage for slider overlay display text
	protected String getSoundVolume(SoundCategory soundCategory) {
		float f = this.gameSettings.getSoundLevel(soundCategory);
		return f == 0.0F ? this.sliderOffText : (int)(f * 100.0F) + "%";
	}

	@SideOnly(Side.CLIENT)
	class Slider extends GuiButton {
		private final SoundCategory soundCategory;
		private final String name;
		public float volume = 1.0F;
		public boolean isSelected; // TODO: what is this value?

		public Slider(int sliderID, int x, int y, SoundCategory soundCategory, boolean isMasterSlider) {
			super(sliderID, x, y, isMasterSlider ? 310 : 150, 20, "");

			this.soundCategory = soundCategory;
			this.name = I18n.format("soundCategory." + soundCategory.getCategoryName(), new Object[0]);
			this.displayString = this.name + ": " + GuiScreenCustomOptionsSounds.this.getSoundVolume(soundCategory);
			this.volume = GuiScreenCustomOptionsSounds.this.gameSettings.getSoundLevel(soundCategory);
		}

		@Override
		protected int getHoverState(boolean mouseOver) {
			return 0; // All sliders are constantly disabled button backgrounds
		}

		@Override
		protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) { // do all that shit
			if (this.visible) {
				if (this.isSelected) {
					this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
					this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);

					mc.gameSettings.setSoundLevel(this.soundCategory, this.volume);
					mc.gameSettings.saveOptions();

					this.displayString = this.name + ": " + GuiScreenCustomOptionsSounds.this.getSoundVolume(this.soundCategory);
				}

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
				this.drawTexturedModalRect(this.xPosition + (int)(this.volume * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
			}
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			// Valid button area is clicked
			if (super.mousePressed(mc, mouseX, mouseY)) {
				// New volume based on slider
				this.volume = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
				this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);

				// Set and save sound level
				mc.gameSettings.setSoundLevel(this.soundCategory, this.volume);
				mc.gameSettings.saveOptions();

				// Update display text based on new sound volume level
				this.displayString = this.name + ": " + GuiScreenCustomOptionsSounds.this.getSoundVolume(this.soundCategory);
				this.isSelected = true;

				// We found something to act upon
				return true;
			} else {
				// We didn't find any GUI to do something within
				return false;
			}
		}

		@Override
		public void playPressSound(SoundHandler soundHandlerIn) {} // TODO

		@Override
		public void mouseReleased(int mouseX, int mouseY) { // TODO
			if (this.isSelected) {
				if (this.soundCategory == SoundCategory.MASTER) {
					float f = 1.0F;
				} else {
					GuiScreenCustomOptionsSounds.this.gameSettings.getSoundLevel(this.soundCategory);
				}

				GuiScreenCustomOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}

			this.isSelected = false;
		}
	}

	@SubscribeEvent
	public void onPlaySoundEvent(PlaySoundEvent event) {
	}
}