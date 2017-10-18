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

package me.hink.tweaker.util;

import org.lwjgl.util.Color;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class ColorUtils {
	public static class ReadableColor {
		public static final Color RED = (Color) Color.RED;						// http://www.color-hex.com/color/ff0000
		public static final Color ORANGE = (Color) Color.ORANGE;				// http://www.color-hex.com/color/ff0000
		public static final Color YELLOW = (Color) Color.YELLOW;				// http://www.color-hex.com/color/ffff00
		public static final Color GREEN = (Color) Color.GREEN;					// http://www.color-hex.com/color/008000
		public static final Color BLUE = (Color) Color.BLUE;					// http://www.color-hex.com/color/0000ff
		public static final Color INDIGO = new Color(75, 0, 130);		// http://www.color-hex.com/color/4b0082
		public static final Color VIOLET = new Color(238, 130, 238);	// http://www.color-hex.com/color/ee82ee
	}

	public static class SpectrumColors {
		public static final int RED		= 0xFF0000; // http://www.color-hex.com/color/ff0000
		public static final int ORANGE	= 0xFFA500; // http://www.color-hex.com/color/ffa500
		public static final int YELLOW	= 0xFFFF00; // http://www.color-hex.com/color/ffff00
		public static final int GREEN	= 0x008000; // http://www.color-hex.com/color/008000
		public static final int BLUE	= 0x0000FF; // http://www.color-hex.com/color/0000ff
		public static final int INDIGO	= 0x4B0082; // http://www.color-hex.com/color/4b0082
		public static final int VIOLET	= 0xEE82EE; // http://www.color-hex.com/color/ee82ee
	}
}
