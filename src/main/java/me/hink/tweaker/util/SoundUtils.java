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

import com.google.common.collect.Lists;

import me.hink.tweaker.TweakerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class SoundUtils {
	private static class IModifiedSound implements ISound {
		ISound originalSound;
		float volumeModifier, changedVolume;

		private IModifiedSound(ISound originalSound, float volumeModifier) {
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

	public static ISound scaleSoundVolume(ISound sound, float volumeModifier) {
		return new IModifiedSound(sound, volumeModifier);
	}

	public static List<String> getRegisteredSounds() {
		SoundRegistry soundRegistry = null;

		soundRegistry = Minecraft.getMinecraft().getSoundHandler().sndRegistry;

//		soundRegistry.spliterator(); // TODO

		if (soundRegistry == null) {
			return Lists.newArrayList();
		}

		List<String> soundNames = new ArrayList<>();

		Iterator iterator = soundRegistry.soundRegistry.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();

			if (entry.getKey() instanceof ResourceLocation) {
				ResourceLocation resourceLocation = (ResourceLocation) entry.getKey();

				String resourcePath = resourceLocation.getResourcePath();
				soundNames.add(resourcePath);
			}
		}

		Collections.sort(soundNames);

		return soundNames;
	}

	public static boolean isValidSoundName(String soundName) {
		for (String s : SoundUtils.getRegisteredSounds()) {
			if (s.toLowerCase().contains(soundName.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public static String getSoundVolume(String soundName) {
		float volume = TweakerMod.soundSettings.getSoundLevel(soundName);

		return volume == 0.0F ? "Off" : (int)(volume * 100.0F) + "%";
	}
}
