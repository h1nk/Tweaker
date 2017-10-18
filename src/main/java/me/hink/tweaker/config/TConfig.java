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

import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static me.hink.tweaker.config.TConfig.Category.*;

/**
 * TODO
 *
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
public class TConfig {
	public static Configuration config;

	public static String customScreenShotName;
	public static String customScreenShotFolderNamePattern;

	public static boolean isCustomScreenShotNameEnabled;
	public static boolean isCustomScreenShotFolderNamePatternEnabled;

	// Tweaks & Features
	public static boolean isDoubleClickMechanicDisabled;
	public static boolean isEnhancedSoundOptionsEnabled;
	public static boolean isHurtCameraEffectEnabled = true;
	public static boolean isPersistentChatEnabled;

	public static boolean isPotionEffectRepositioningEnabled;
	public static boolean isSearchableControlOptionsGUIEnabled;
	public static boolean isAutomaticRespawnScreen;
	public static boolean isRespawnButtonCooldownEnabled;

	// Bug Fixes
	public static boolean isHeadLookYawCalculationFixEnabled = false;
	public static boolean isRightArmRidableEntityRenderFixEnabled = true;
	public static boolean isToggleFullscreenResizeFixEnabled = true;

	// Plugin Channel Messaging
	public static boolean blockInboundMessages;
	public static boolean blockOutboundMessages;

	// Debug
	public static boolean isPluginChannelMessageLoggingEnabled = false;
	public static boolean isDebugBlockHighlightingEnabled = false;
	public static boolean isGuiLoggingEnabled = false;

	public enum Category {
		SCREENSHOTS("screenshots", "Screenshots"),
		FEATURES("features", "Features"),
		BUG_FIXES("bug-fixes", "Bug Fixes"),
		PLUGIN_MESSAGING("plugin-messaging", "Plugin Channel Messaging"),
		DEBUG("debug", "Debug");

		private final String category;
		private final String name;

		Category(final String category, final String name) {
			this.category = category;
			this.name = name;
		}

		public String getCategory() {
			return category;
		}

		public String getName() {
			return name;
		}
	}

	public static void startConfig(File configFile) {
		if (config == null) {
			config = new Configuration(configFile);
			loadConfig();
		}
	}

	public static void loadConfig() {
		config.setCategoryComment(SCREENSHOTS.category, "Screenshot settings");
		config.setCategoryComment(FEATURES.category, "Feature settings");
		config.setCategoryComment(BUG_FIXES.category, "Bug Fixes");
		config.setCategoryComment(PLUGIN_MESSAGING.category, "Plugin Messaging");

		config.setCategoryLanguageKey(SCREENSHOTS.category, "tweaker.config-gui.category.screenshots");
		config.setCategoryLanguageKey(FEATURES.category, "tweaker.config-gui.category.features");
		config.setCategoryLanguageKey(BUG_FIXES.category, "tweaker.config-gui.category.bug-fixes");
		config.setCategoryLanguageKey(PLUGIN_MESSAGING.category, "tweaker.config-gui.category.plugin-messaging");

		config.setCategoryRequiresMcRestart(BUG_FIXES.category, true);

		isCustomScreenShotNameEnabled = config.getBoolean("isCustomScreenShotNameEnabled",
			SCREENSHOTS.getCategory(),
			true,
			"Enable custom screenshot file naming",
			"tweaker.config-gui.category.screenshots.is-custom-screenshot-name-enabled");

		customScreenShotName = config.getString("customScreenShotName",
			SCREENSHOTS.category,
			"%uuid",
			"Custom screenshot file naming pattern",
			"tweaker.config-gui.category.screenshots.custom-screenshot-name");

		// Tweaks & Features
		isDoubleClickMechanicDisabled = config.getBoolean("Double Click Inventory Mechanic",
				FEATURES.category,
				true,
				"Toggle option for the double click container inventory GUI stack mechanic",
				"test");

		isHurtCameraEffectEnabled = config.getBoolean("Hurt Camera Effect",
			FEATURES.category,
			true,
			"Toggle option for the the hurt (hit punch) entity render effect",
			"");

		isSearchableControlOptionsGUIEnabled = config.getBoolean("Searchable Control Options GUI",
				FEATURES.category,
				true,
				"Enabled a search bar in the controls GUI screen that enabled you to search through the available keybinds.",
				"");

		isEnhancedSoundOptionsEnabled = config.getBoolean("Enhanced Sound Options",
				FEATURES.category,
				true,
				"Enhanced sound setting features, like customized sound profiles and the ability to individually" +
				" scale the volume of each registered game sound individually",
				"");

		isPersistentChatEnabled = config.getBoolean("Persistent Chat",
				FEATURES.category,
				true,
				"Remember the state of typed chat when dying, changing worlds, or leaving bed",
				"");

		isAutomaticRespawnScreen = config.getBoolean("Automatically Respawn",
				FEATURES.category,
				false,
				"Automatically select respawn as the option respawn whenever presented a respawn/death screen",
				"");

		isRespawnButtonCooldownEnabled = config.getBoolean("Respawn Button Enable Delay",
				FEATURES.category,
				false,
				"Toggle the one second wait for Game Over GUI buttons to be enabled",
				"");

		isPotionEffectRepositioningEnabled = config.getBoolean("Potion Effect Repositioning",
				FEATURES.category,
				false,
				"Whether or not to reposition the player inventory GUI container when rendering potion effects " +
				"on the side of the screen.",
				"");

		// Bug Fixes
		isHeadLookYawCalculationFixEnabled = config.getBoolean("[MC-67665] Head yaw look calculation bug fix",
				BUG_FIXES.category,
				true,
				"Fixes head yaw look vector calculation bug.",
				"");

		isRightArmRidableEntityRenderFixEnabled = config.getBoolean("[MC-1349] Right arm ridable entity render bug fix",
				BUG_FIXES.category,
				true,
				"Fixes visual render glitch with how the right arm of the player model is rendered, the bug was easily" +
				"reproducible when riding a rideable entity",
				"");

		isToggleFullscreenResizeFixEnabled = config.getBoolean("[MC-68754] Exiting fullscreen disables window resize",
				BUG_FIXES.category,
				true,
				"",
				"");

		// Plugin Channel Messaging
		blockInboundMessages = config.getBoolean("Block Inbound Communication",
				PLUGIN_MESSAGING.category,
				false,
				"Enable blocking of inbound plugin channel message packet communication",
				"");

		blockOutboundMessages = config.getBoolean("Block Outbound Communication",
			PLUGIN_MESSAGING.category,
			false,
			"Enable blocking of outbound plugin channel message packet communication",
			"");

		isPluginChannelMessageLoggingEnabled = config.getBoolean("Log Plugin Channel Messages",
				DEBUG.category,
				false,
				"Enable logging of plugin channel messaging communication",
				"");

		isDebugBlockHighlightingEnabled = config.getBoolean("Block Highlighting",
				DEBUG.category,
				false,
				"",
				"");

		isGuiLoggingEnabled = config.getBoolean("GUI Logging",
				DEBUG.category,
				false,
				"",
				"");

		if (config.hasChanged()) {
			config.save();
		}
	}
}
