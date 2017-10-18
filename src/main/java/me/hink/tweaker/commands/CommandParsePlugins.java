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

package me.hink.tweaker.commands;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;

import me.hink.tweaker.event.events.ReadPacketEvent;
import me.hink.tweaker.util.PacketUtils;

/**
 * TODO
 *
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
public class CommandParsePlugins extends ClientSideCommandBase {
	@Override
	public String getCommandName() {
		return "parse-plugins";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		Minecraft mc = Minecraft.getMinecraft();

		final String messageText
				= String.format("Sending %s packet and awaiting response...", C14PacketTabComplete.class.getSimpleName());
		final ChatComponentText chatMessage = new ChatComponentText(messageText);

		mc.thePlayer.addChatMessage(chatMessage);

		MinecraftForge.EVENT_BUS.register(this);
		mc.getNetHandler().addToSendQueue(new C14PacketTabComplete("/"));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onReadPacketEvent(ReadPacketEvent event) {
		if (event.packet != null && event.packet instanceof S3APacketTabComplete) {
			Minecraft mc = Minecraft.getMinecraft();

			S3APacketTabComplete packetTabComplete = (S3APacketTabComplete) event.packet;

			event.setCanceled(true);

			ArrayList<String> headers = PacketUtils.parsePluginHeaders(packetTabComplete.func_149630_c());

			for (String s : headers) {
				mc.thePlayer.addChatMessage(new ChatComponentText("- " + s));
			}

			String responseMessageText = S3APacketTabComplete.class.getSimpleName() + ' ' + "packet received!";
			final ChatComponentText responseMessage = new ChatComponentText(responseMessageText);

			mc.thePlayer.addChatMessage(responseMessage);

			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
}
