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

package me.hink.tweaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.minecraft.client.Minecraft;

import me.hink.tweaker.binds.KeyBindings;
import me.hink.tweaker.commands.CommandParsePlugins;
import me.hink.tweaker.commands.CommandSavePlayerList;
import me.hink.tweaker.config.SoundSettings;
import me.hink.tweaker.event.forge.ClientEventHandler;
import me.hink.tweaker.event.forge.KeyInputHandler;
import me.hink.tweaker.event.forge.PacketEventHandler;
import me.hink.tweaker.event.forge.ScreenShotEventHandler;

import static me.hink.tweaker.TweakerMod.MODID;
import static me.hink.tweaker.config.TConfig.startConfig;

/**
 * This is the base Forge mod class for FML to call
 *
 * @see net.minecraftforge.fml.common.Mod
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
@Mod(modid = MODID,
	name = "Tweaker",
	modLanguage = "java",
	updateJSON = "https://h1nk.github.io/mods/tweaker/update.json",
	version = "0.0.1",
	guiFactory = "me.hink.tweaker.gui.TModGuiFactory",
	clientSideOnly = true)
public class TweakerMod {
	public static final String MODID = "tweaker";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static SoundSettings soundSettings;

	@Mod.Instance(value = MODID)
	public static TweakerMod instance;

	@Mod.EventHandler
	public static void onFMLPreInitializationEvent(FMLPreInitializationEvent event) {
		startConfig(event.getSuggestedConfigurationFile());

		KeyBindings.register();

		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

		ClientCommandHandler.instance.registerCommand(new CommandParsePlugins());
		ClientCommandHandler.instance.registerCommand(new CommandSavePlayerList());
	}

	@Mod.EventHandler
	public static void onFMLInitializationEvent(FMLInitializationEvent event) {
		LOGGER.info("Hello Minecraft! :)");
	}

	@Mod.EventHandler
	public static void onFMLPostInitializationEvent(FMLPostInitializationEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		soundSettings = new SoundSettings(mc, mc.mcDataDir);

		MinecraftForge.EVENT_BUS.register(new PacketEventHandler());
		MinecraftForge.EVENT_BUS.register(new ScreenShotEventHandler());
	}
}
