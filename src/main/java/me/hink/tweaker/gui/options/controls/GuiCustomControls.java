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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class GuiCustomControls extends GuiScreen {
	private static final GameSettings.Options[] optionsArr = new GameSettings.Options[] {GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN};
	/** A reference to the screen object that created this. Used for navigating between screens. */
	private GuiScreen parentScreen;
	private String screenTitle = "Controls";
	/** Reference to the GameSettings object. */
	private GameSettings options;
	/** The ID of the button that has been pressed. */
	KeyBinding buttonId = null;
	private long time;
	private GuiCustomKeyBindingList keyBindingList;
	private GuiButton buttonReset;
	private static final String __OBFID = "CL_00000736";

	private static GuiTextField search;

	Pattern modSearch = Pattern.compile("^@(?:[Mm][Oo][Dd])(?:(?:\\.)|(?:\\s+))(.*)$");

	public GuiCustomControls(GuiScreen screen, GameSettings settings) {
		this.parentScreen = screen;
		this.options = settings;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.keyBindingList = new GuiCustomKeyBindingList(this, this.mc, null);
		this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(this.buttonReset = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0])));
		this.screenTitle = I18n.format("controls.title", new Object[0]);
		int i = 0;
		GameSettings.Options[] aoptions = optionsArr;
		int j = aoptions.length;

		for (int k = 0; k < j; ++k) {
			GameSettings.Options options = aoptions[k];

			if (options.getEnumFloat()) {
				this.buttonList.add(new GuiOptionSlider(options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), options));
			} else {
				this.buttonList.add(new GuiOptionButton(options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), options, this.options.getKeyBinding(options)));
			}

			++i;
		}

		// Search Text Field
		search = new GuiTextField(0, this.mc.fontRendererObj, this.width / 2 + 5 + 1, 45 - 1, 145 + 3, 15 + 1);
		search.setFocused(false);
		search.setCanLoseFocus(true);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.search.updateCursorCounter();
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		this.keyBindingList.handleMouseInput();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else if (button.id == 201) {
			KeyBinding[] akeybinding = this.mc.gameSettings.keyBindings;
			int i = akeybinding.length;

			for (int j = 0; j < i; ++j) {
				KeyBinding keybinding = akeybinding[j];
				keybinding.setKeyCode(keybinding.getKeyCodeDefault());
			}

			KeyBinding.resetKeyBindingArrayAndHash();
		} else if (button.id < 100 && button instanceof GuiOptionButton) {
			this.options.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
			button.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (this.buttonId != null) {
			this.options.setOptionKeyBinding(this.buttonId, -100 + mouseButton);
			this.buttonId = null;
			KeyBinding.resetKeyBindingArrayAndHash();
		} else if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}

		// Search Text Field
		this.search.mouseClicked(mouseX, mouseY, mouseButton);

		// Right clicking within the search field clears like any other Forge mod search field (like the mod info list, JEI or AE2 terminals)
		if (mouseButton == 1 && mouseX >= search.xPosition && mouseX < search.xPosition + search.width && mouseY >= search.yPosition && mouseY < search.yPosition + search.height) {
			search.setText("");
//			this.keyBindingList = new GuiSoundList(this, this.mc, null); // TODO
		}
	}

	/**
	 * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
	 */
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (state != 0 || !this.keyBindingList.mouseReleased(mouseX, mouseY, state)) {
			super.mouseReleased(mouseX, mouseY, state);
		}
	}

//	/**
//	 * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
//	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
//	 */
//	@Override
//	protected void keyTyped(char typedChar, int keyCode) throws IOException {
//		super.keyTyped(typedChar, keyCode);
//		System.out.println("test");
//
//		if (this.buttonId != null) {
//			if (keyCode == 1) {
//				this.options.setOptionKeyBinding(this.buttonId, 0);
//			}
//			else if (keyCode != 0) {
//				this.options.setOptionKeyBinding(this.buttonId, keyCode);
//			}
//			else if (typedChar > 0) {
//				this.options.setOptionKeyBinding(this.buttonId, typedChar + 256);
//			}
//
//			this.buttonId = null;
//			this.time = Minecraft.getSystemTime();
//
//			KeyBinding.resetKeyBindingArrayAndHash();
//		} else if (this.search.textboxKeyTyped(typedChar, keyCode)) { // Typed character in search text field
//			// Create new updated list
//			System.out.println("test");
//			this.keyBindingList = new GuiCustomKeyBindingList(this, this.mc, this.search.getText());
//		}
//	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.buttonId != null) {
			if (keyCode == 1) {
				this.options.setOptionKeyBinding(this.buttonId, 0);
			} else if (keyCode != 0) {
				this.options.setOptionKeyBinding(this.buttonId, keyCode);
			} else if (typedChar > 0) {
				this.options.setOptionKeyBinding(this.buttonId, typedChar + 256);
			}

			this.buttonId = null;
			this.time = Minecraft.getSystemTime();

			KeyBinding.resetKeyBindingArrayAndHash();
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// GUI Background
		this.drawDefaultBackground();

		// GUI Title Text
		this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 8, 16777215);

		// GUI Keybinding Scroll List
		this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);

		boolean flag = true;
		KeyBinding[] akeybinding = this.options.keyBindings;
		int k = akeybinding.length;

		for (int l = 0; l < k; ++l) {
			KeyBinding keybinding = akeybinding[l];

			if (keybinding.getKeyCode() != keybinding.getKeyCodeDefault()) {
				flag = false;
				break;
			}
		}

		// Reset All Sounds Button
		this.buttonReset.enabled = !flag;

		// Search Text Field
		search.drawTextBox();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
