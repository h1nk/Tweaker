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

package me.hink.tweaker.gui.options.controls;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Strings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class GuiCustomKeyBindingList extends GuiListExtended {
	private final GuiCustomControls parentControlsGUI;
	private final Minecraft mc;
	private GuiListExtended.IGuiListEntry[] listEntries;

	private int maxListLabelWidth = 0;

	GuiCustomKeyBindingList(GuiCustomControls controls, Minecraft mc, String searchFilter) {
		super(mc, controls.width, controls.height, 63, controls.height - 32, 20);

		this.parentControlsGUI = controls;
		this.mc = mc;

		KeyBinding[] gameKeybinds = ArrayUtils.clone(mc.gameSettings.keyBindings);

		this.listEntries = new GuiListExtended.IGuiListEntry[gameKeybinds.length + KeyBinding.getKeybinds().size()];

		Arrays.sort(gameKeybinds);

		int i = 0;
		String tempKeyCategory = null;

		for (KeyBinding keyBinding : gameKeybinds) {
			String keyCategory = keyBinding.getKeyCategory();

			if (!keyCategory.equals(tempKeyCategory)) {
				tempKeyCategory = keyCategory;
				this.listEntries[i++] = new CategoryEntry(keyCategory);
			}

			int keybindDescriptionWidth = mc.fontRendererObj.getStringWidth(I18n.format(keyBinding.getKeyDescription()));

			// Update maximum keybind description width
			if (keybindDescriptionWidth > this.maxListLabelWidth) {
				this.maxListLabelWidth = keybindDescriptionWidth;
			}

			// Add a new keybind entry to the list
			this.listEntries[i++] = new KeyEntry(keyBinding, null);
		}

		if (!Strings.isNullOrEmpty(searchFilter)) {
		}
	}

	protected int getSize() {
		return this.listEntries.length;
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
		return this.listEntries[p_148180_1_];
	}

	protected int getScrollBarX() {
		return super.getScrollBarX() + 15;
	}

	/**
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return super.getListWidth() + 32;
	}

	@SideOnly(Side.CLIENT)
	public class CategoryEntry implements GuiListExtended.IGuiListEntry {
		private final String labelText;
		private final int labelWidth;

		CategoryEntry(String p_i45028_2_) {
			this.labelText = I18n.format(p_i45028_2_, new Object[0]);
			this.labelWidth = GuiCustomKeyBindingList.this.mc.fontRendererObj.getStringWidth(this.labelText);
		}

		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
			GuiCustomKeyBindingList.this.mc.fontRendererObj.drawString(this.labelText, GuiCustomKeyBindingList.this.mc.currentScreen.width / 2 - this.labelWidth / 2, y + slotHeight - GuiCustomKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT - 1, 16777215);
		}

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
			return false;
		}

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
		 */
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}

		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
	}

	@SideOnly(Side.CLIENT)
	public class KeyEntry implements GuiListExtended.IGuiListEntry {
		/** The keyBinding specified for this KeyEntry */
		private final KeyBinding keyBinding;
		/** The localized key description for this KeyEntry */
		private final String keyDescription;
		private final GuiButton btnChangeKeyBinding;
		private final GuiButton btnReset;

		KeyEntry(KeyBinding keyBinding, Object o) {
			this(keyBinding);
		}

		private KeyEntry(KeyBinding keyBinding) {
			this.keyBinding = keyBinding;
			this.keyDescription = I18n.format(keyBinding.getKeyDescription(), new Object[0]);
			this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 18, I18n.format(keyBinding.getKeyDescription(), new Object[0]));
			this.btnReset = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
		}

		String getKeyDescription() {
			return this.keyDescription;
		}

		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
			boolean flag1 = GuiCustomKeyBindingList.this.parentControlsGUI.buttonId == this.keyBinding;
			GuiCustomKeyBindingList.this.mc.fontRendererObj.drawString(this.keyDescription, x + 90 - GuiCustomKeyBindingList.this.maxListLabelWidth, y + slotHeight / 2 - GuiCustomKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);
			this.btnReset.xPosition = x + 190;
			this.btnReset.yPosition = y;
			this.btnReset.enabled = this.keyBinding.getKeyCode() != this.keyBinding.getKeyCodeDefault();
			this.btnReset.drawButton(GuiCustomKeyBindingList.this.mc, mouseX, mouseY);
			this.btnChangeKeyBinding.xPosition = x + 105;
			this.btnChangeKeyBinding.yPosition = y;
			this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keyBinding.getKeyCode());
			boolean flag2 = false;

			if (this.keyBinding.getKeyCode() != 0) {
				KeyBinding[] akeybinding = GuiCustomKeyBindingList.this.mc.gameSettings.keyBindings;
				int l1 = akeybinding.length;

				for (KeyBinding keyBinding : akeybinding) {
					if (keyBinding != this.keyBinding && keyBinding.getKeyCode() == this.keyBinding.getKeyCode()) {
						flag2 = true;
						break;
					}
				}
			}

			if (flag1) {
				this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
			}
			else if (flag2) {
				this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
			}

			this.btnChangeKeyBinding.drawButton(GuiCustomKeyBindingList.this.mc, mouseX, mouseY);
		}

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
			if (this.btnChangeKeyBinding.mousePressed(GuiCustomKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
				GuiCustomKeyBindingList.this.parentControlsGUI.buttonId = this.keyBinding;
				return true;
			}
			else if (this.btnReset.mousePressed(GuiCustomKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
				GuiCustomKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.keyBinding, this.keyBinding.getKeyCodeDefault());
				KeyBinding.resetKeyBindingArrayAndHash();
				return true;
			}
			else {
				return false;
			}
		}

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
		 */
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			this.btnChangeKeyBinding.mouseReleased(x, y);
			this.btnReset.mouseReleased(x, y);
		}

		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
	}
}
