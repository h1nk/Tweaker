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

package me.hink.tweaker.event.events;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This event is fired before a screenshot is taken
 * This event is fired on the {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
 * This event is {@link Cancelable}
 *
 * {@link #screenshotFile} contains the file the screenshot will be saved to
 * {@link #bufferedImage} contains the {@link BufferedImage} that will be saved
 * {@link #chatComponent} contains the {@link ChatComponentText} to be returned.
 * If {@code null}, the default vanilla message will be used instead
 *
 * @see			<a href="https://github.com/MinecraftForge/MinecraftForge/pull/2602">MinecraftForge/MinecraftForge#2602</a>
 * @see			<a href="https://github.com/MinecraftForge/MinecraftForge/pull/2828">MinecraftForge/MinecraftForge#2828</a>
 *
 * @author		<a href="https://github.com/h1nk">h1nk</a>
 *
 * @since 0.0.1
 */
@Cancelable
public class ScreenShotEvent extends Event {
//	public static final IChatComponent DEFAULT_CANCEL_MESSAGE = new ChatComponentText("Screenshot canceled");
	private BufferedImage bufferedImage;
	public File screenshotFile;

	public IChatComponent chatComponent = null;

	public ScreenShotEvent(BufferedImage image, File screenshotFile) {
		this.bufferedImage = image;
		this.screenshotFile = screenshotFile;
	}

	public File getScreenShot() {
		return screenshotFile;
	}

	public IChatComponent geCancelMessage() {
		return new ChatComponentText("Screenshot canceled...");
	}
}