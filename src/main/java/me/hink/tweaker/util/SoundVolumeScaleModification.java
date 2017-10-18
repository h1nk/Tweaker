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

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class SoundVolumeScaleModification implements ISound {
	final ISound originalSound;
	final float volumeModifier;

	public SoundVolumeScaleModification(ISound originalSound, float volumeModifier) {
		this.originalSound = originalSound;
		this.volumeModifier = volumeModifier;
	}

	@Override
	public ResourceLocation getSoundLocation() {
		return this.originalSound.getSoundLocation();
	}

	@Override
	public boolean canRepeat() {
		return this.originalSound.canRepeat();
	}

	@Override
	public int getRepeatDelay() {
		return this.originalSound.getRepeatDelay();
	}

	@Override
	public float getVolume() {
		return (originalSound.getVolume() * volumeModifier);
	}

	@Override
	public float getPitch() {
		return this.originalSound.getPitch();
	}

	@Override
	public float getXPosF() {
		return this.originalSound.getXPosF();
	}

	@Override
	public float getYPosF() {
		return this.originalSound.getYPosF();
	}

	@Override
	public float getZPosF() {
		return this.originalSound.getZPosF();
	}

	@Override
	public AttenuationType getAttenuationType() {
		return this.originalSound.getAttenuationType();
	}
}