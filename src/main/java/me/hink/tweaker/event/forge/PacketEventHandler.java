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

package me.hink.tweaker.event.forge;

import joptsimple.internal.Strings;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

import me.hink.tweaker.TweakerMod;
import me.hink.tweaker.event.events.ReadPacketEvent;
import me.hink.tweaker.event.events.SendPacketEvent;

import static me.hink.tweaker.config.TConfig.*;

/**
 * You don't need to know about me.
 *
 * Custom implementation of http://www.minecraftforge.net/forum/topic/28511-event-packet-send-and-read-events
 * for blocking plugin channel communication. This should even allow players to use Badlion's backdoor'ed version of Vape.
 * although the message response detection doesn't use channel messages.
 *
 * @author <a href="https://github.com/h1nk">h1nk</a>
 * @since 0.0.1
 */
@SideOnly(Side.CLIENT)
public class PacketEventHandler {
	@SubscribeEvent
	public void onReadPacketEvent(ReadPacketEvent event) {
		if (event.packet instanceof S3FPacketCustomPayload) {
			if (isPluginChannelMessageLoggingEnabled) {
				S3FPacketCustomPayload packet = (S3FPacketCustomPayload) event.packet;

				String channelName = packet.getChannelName();
				PacketBuffer packetBuffer = packet.getBufferData();

				String message = new String(packetBuffer.array());

				if (!Strings.isNullOrEmpty(message)) {
					String logMessage = "";

					logMessage += "Custom payload received: ";
					logMessage += String.format("\n\tChannel Name: %s \n\tMessage Payload: %s", channelName, message);

					TweakerMod.LOGGER.info(logMessage);
				}
			}

			if (blockInboundMessages) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onSendPacketEvent(SendPacketEvent event) {
		if (event.packet instanceof C17PacketCustomPayload) {
			if (isPluginChannelMessageLoggingEnabled) {
				C17PacketCustomPayload packet = (C17PacketCustomPayload) event.packet;

				String channelName = packet.getChannelName();
				PacketBuffer packetBuffer = packet.getBufferData();

				String message = new String(packetBuffer.array());

				if (!Strings.isNullOrEmpty(message)) {
					String logMessage = "";

					logMessage += "Custom payload sent: ";
					logMessage += String.format("\n\tChannel Name: %s \n\tMessage Payload: %s", channelName, message);

					TweakerMod.LOGGER.info(logMessage);
				}
			}

			if (blockOutboundMessages) {
				event.setCanceled(true);
			}
		}
	}
}
