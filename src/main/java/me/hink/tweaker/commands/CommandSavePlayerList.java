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

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * TODO
 *
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
public class CommandSavePlayerList extends ClientSideCommandBase {
	@Override
	public String getCommandName() {
		return "save-player-list";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		Minecraft mc = Minecraft.getMinecraft();

		StringBuilder str = new StringBuilder();

		Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();

		for (NetworkPlayerInfo player : players) {
			str.append(player.getGameProfile().getName()).append(System.getProperty("line.separator"));
		}

		String filePath = System.getProperty("user.home");

		filePath += File.separator
			+ "Desktop"
			+ File.separator
			+ "account_names.txt";

		filePath = filePath.replaceAll("\\\\", "\\\\\\\\");

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

			out.write(str.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mc.thePlayer.addChatMessage(new ChatComponentText("Dumped account list to text file in home directory..."));
		mc.thePlayer.addChatMessage(new ChatComponentText(filePath));
	}
}
