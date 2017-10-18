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

package me.hink.tweaker.gui;

import me.hink.tweaker.TweakerMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hink.tweaker.config.TConfig;

import static me.hink.tweaker.config.TConfig.config;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class TModConfigGuiScreen extends GuiConfig {
	public TModConfigGuiScreen(GuiScreen parentScreen) {
		super(parentScreen,
				getConfigElements(),
			TweakerMod.MODID,
				false,
				false,
				"Tweaker Mod Configuration GUI");
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<>();

		for (TConfig.Category category : TConfig.Category.values()) {
			list.add(categoryElement(category.getCategory(), category.getName(), ""));
		}

		return list;
	}

	private static IConfigElement categoryElement(String category, String name, String langKey) {
		return new DummyConfigElement.DummyCategoryElement(name, langKey,
				new ConfigElement(config.getCategory(category)).getChildElements());
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) throws IOException {
		super.mouseClicked(x, y, mouseEvent);
	}

	@Override
	protected void mouseReleased(int x, int y, int mouseEvent) {
		super.mouseReleased(x, y, mouseEvent);
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey) {
		super.keyTyped(eventChar, eventKey);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void drawToolTip(List stringList, int x, int y) {
		super.drawToolTip(stringList, x, y);
	}
}
