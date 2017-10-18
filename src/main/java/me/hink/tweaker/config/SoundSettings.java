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

package me.hink.tweaker.config;

import com.google.common.collect.Maps;

import me.hink.tweaker.TweakerMod;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.List;
import java.util.Map;

import me.hink.tweaker.util.SoundUtils;

import static me.hink.tweaker.TweakerMod.LOGGER;

/**
 * TODO
 *
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
public class SoundSettings {
	private File optionsFile;
	private Minecraft minecraft;

	private Map<String, Float> mapSoundLevels;

	public SoundSettings(Minecraft minecraft, File file) {
		this.mapSoundLevels = Maps.newHashMap();

		for (String soundName : SoundUtils.getRegisteredSounds()) {
			mapSoundLevels.put(soundName, 1.0f);
		}

		this.minecraft = minecraft;
		this.optionsFile = new File(file, "sound-options.txt");

		this.loadOptions();
		this.saveOptions();
	}

	public SoundSettings() {} // TODO

	public void loadOptions() {
		try {
			// If the sound-options.txt file does not exist, create it
			if (!this.optionsFile.exists() && optionsFile.createNewFile()) {
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(optionsFile));

				for (String soundName : this.mapSoundLevels.keySet()) {
					bufferedWriter.append("sound_" + soundName + ":" + this.mapSoundLevels.get(soundName) + System.lineSeparator());
				}

				bufferedWriter.close();
			} else {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(this.optionsFile));

				String string = "";

				this.mapSoundLevels.clear();

				List<String> soundNames = SoundUtils.getRegisteredSounds();

				while ((string = bufferedReader.readLine()) != null) {
					try {
						String[] strings = string.split(":");

						for (String soundName : soundNames) {
							if (strings[0].equalsIgnoreCase("sound_" + soundName)) {
								this.mapSoundLevels.put(soundName, Float.valueOf(strings[1]));
							}
						}
					} catch (Exception exception) {
						TweakerMod.LOGGER.warn("Skipping bad option: " + string);
					}
				}
			}
		} catch (Exception exception) {
			TweakerMod.LOGGER.error("Failed to load options", exception);
		}
	}

	public void saveOptions() {
//		if (net.minecraftforge.fml.client.FMLClientHandler.instance().isLoading()) return;

		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(this.optionsFile));

			for (String soundName : this.mapSoundLevels.keySet()) {
				printWriter.println("sound_" + soundName + ":" + this.mapSoundLevels.get(soundName));
			}

			printWriter.close();
		} catch (Exception exception) {
			TweakerMod.LOGGER.error("Failed to save options", exception);
		}
	}

	public float getSoundLevel(String soundName) {
		return this.mapSoundLevels.getOrDefault(soundName, 1.0F);
	}

	public void setSoundLevel(String soundName, float volume) {
		this.mapSoundLevels.put(soundName, volume);
		this.saveOptions();
	}

	public void resetSoundLevels() {
		for (String soundName : SoundUtils.getRegisteredSounds()) {
			this.setSoundLevel(soundName, 1.0f);
		}
	}
}
