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

package me.hink.tweaker.event;

import net.minecraftforge.common.MinecraftForge;

import java.awt.image.BufferedImage;
import java.io.File;

import me.hink.tweaker.event.events.ScreenShotEvent;

/**
 * TODO
 *
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
public class CustomHooksClient {
	public static ScreenShotEvent onScreenShot(BufferedImage bufferedImage, File screenShotFile) {
		ScreenShotEvent event = new ScreenShotEvent(bufferedImage, screenShotFile);
		MinecraftForge.EVENT_BUS.post(event);

		return event;
	}
}
