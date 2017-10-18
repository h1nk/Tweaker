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

package me.hink.tweaker.asm;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

import me.hink.tweaker.config.TConfig;

import static me.hink.tweaker.TweakerMod.LOGGER;
import static org.objectweb.asm.Opcodes.*;

/**
 * This class provides the ASM bytecode manipulation for the coremod
 * functionality by implementing Minecraft's
 * {@link net.minecraft.launchwrapper.IClassTransformer}.
 *
 * <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.12.0/highlight.min.js"></script>
 * <script>hljs.initHighlightingOnLoad();</script>
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class ClassTransformer implements IClassTransformer {
	private static final int NONE = 0;

	private static final String[] transformationClasses = {
		"net.minecraft.network.NetworkManager",					// 0 (ReadPacketEvent)
		"net.minecraft.client.network.NetHandlerPlayClient",	// 1 (SendPacketEvent) TODO
		"net.minecraft.util.ScreenShotHelper",					// 2 (ScreenShotEvent) FIXME
		"net.minecraft.client.renderer.entity.RenderPlayer",	// 3
		"net.minecraft.entity.EntityLivingBase",				// 4
		"net.minecraft.client.renderer.EntityRenderer",			// 5
		"net.minecraft.client.gui.inventory.GuiContainer",		// 6
		"net.minecraft.client.Minecraft",						// 7
	};

	private List<String> transformationClassesList = Arrays.asList(transformationClasses);

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		boolean isObfuscated = !name.equals(transformedName);
		int classIndex = transformationClassesList.indexOf(transformedName);

		return classIndex != -1 ? transform(classIndex, basicClass, isObfuscated) : basicClass;
	}

	private static byte[] transform(int index, byte[] transformationClass, boolean isObfuscated) {
		try {
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(transformationClass);
			classReader.accept(classNode, NONE);

			switch (index) {
				case 0: // net.minecraft.network.NetworkManager
					transformNetworkManager(classNode, isObfuscated);			// (ReadPacketEvent)
					break;
				case 1: // net.minecraft.client.network.NetHandlerPlayClient
					transformNetHandlerPlayClient(classNode, isObfuscated);		// (SendPacketEvent)
					break;
				case 2: // net.minecraft.util.ScreenShotHelper
//					transformScreenShotHelper(classNode, isObfuscated);			// (ScreenShotEvent)
					break;
				case 3: // net.minecraft.client.renderer.entity.RenderPlayer
					transformRenderPlayer(classNode, isObfuscated);
					break;
				case 4: // net.minecraft.entity.EntityLivingBase
					transformEntityLivingBase(classNode, isObfuscated);
					break;
				case 5: // net.minecraft.client.renderer.EntityRenderer
					transformEntityRenderer(classNode, isObfuscated);
					break;
				case 6: // net.minecraft.client.gui.inventory.GuiContainer
					transformGuiContainer(classNode, isObfuscated);
					break;
				case 7: // net.minecraft.client.Minecraft
					transformMinecraft(classNode, isObfuscated);
					break;
			}

			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			classNode.accept(classWriter);

			return classWriter.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return transformationClass;
	}

	/**
	 * This method provides the ASM class transformer routine for
	 * Vanilla Minecraft class {@see net.minecraft.network.NetworkManager}
	 *
	 * <h3>1.8.9 <a href="http://export.mcpbot.bspk.rs/stable/1.8.9/">stable_22</a> MCP mappings</h3>
	 * <table>
	 *     <tr>
	 *         <th title="Package Name">Pkg Name</th>
	 *         <th title="Searge Name">Srg Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="net.minecraft.network">net/minecraft/network</th>
	 *         <th>NetworkManager</th>
	 *         <th>ek</th>
	 *     </tr>
	 * </table>
	 *
	 * <hr>
	 *
	 * <h4>net.minecraft.network.NetworkManager#channelRead0(io.netty.channel.ChannelHandlerContext, net.minecraft.network.Packet)</h4>
	 * <table>
	 *     <tr>
	 *         <th title="MCP Name">MCP Name</th>
	 *         <th title="Searge Name">SRG Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *         <th title="Searge Descriptor (Unobfuscated)">SRG Descriptor (Unobfuscated)</th>
	 *         <th title="Searge Descriptor (Obfuscated)">SRG Descriptor (Obfuscated)</th>
	 *     </tr>
	 *     <tr>
	 *         <th></th>
	 *         <th>channelRead0</th>
	 *         <th>a</th>
	 *         <th>(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V</th>
	 *         <th title="net/minecraft/network/Packet --> ff">(Lio/netty/channel/ChannelHandlerContext;Lff;)V</th>
	 *     </tr>
	 * </table>
	 *
	 * <pre class="code"><code class="java">
	 * // net.minecraft.network.NetworkManager
	 * protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) throws Exception {
	 * +   if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new me.hink.tweaker.event.events.ReadPacketEvent(p_channelRead0_2_)))
	 * +   {
	 * +       return;
	 * +   }
	 *     if (this.channel.isOpen())
	 *     {
	 *         try
	 *         {
	 *             p_channelRead0_2_.processPacket(this.packetListener);
	 *         }
	 *         catch (ThreadQuickExitException var4)
	 *         {
	 *             ;
	 *         }
	 *     }
	 * }
	 * </code></pre>
	 *
	 * <hr>
	 *
	 * <h4>net.minecraft.network.NetworkManager#createNetworkManagerAndConnect(java.net.InetAddress, int, boolean)</h4>
	 * <table>
	 *     <tr>
	 *         <th title="MCP Name">MCP Name</th>
	 *         <th title="Searge Name">SRG Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *         <th title="Searge Descriptor (Unobfuscated)">SRG Descriptor (Unobfuscated)</th>
	 *         <th title="Searge Descriptor (Obfuscated)">SRG Descriptor (Obfuscated)</th>
	 *     </tr>
	 *     <tr>
	 *         <th>createNetworkManagerAndConnect</th>
	 *         <th>func_181124_a</th>
	 *         <th>a</th>
	 *         <th>(Ljava/net/InetAddress;IZ)Lnet/minecraft/network/NetworkManager;</th>
	 *         <th title="net/minecraft/network/NetworkManager --> ek">(Ljava/net/InetAddress;IZ)Lek;</th>
	 *     </tr>
	 * </table>
	 *
	 * <pre class="code"><code class="java">
	 * // TODO
	 * </code></pre>
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformNetworkManager(ClassNode classNode, boolean isObfuscated) {
		final String METHOD_CHANNEL_READ_0_NAME = isObfuscated ? "a" : "channelRead0";
		// net/minecraft/network/Packet --> ff
		final String METHOD_CHANNEL_READ_0_DESCRIPTOR = isObfuscated ? "(Lio/netty/channel/ChannelHandlerContext;Lff;)V" : "(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V";

		String mappedMethodName;

		for (MethodNode method : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, method.name, method.desc);

			// There's no MCP name mapping, just a SRG name...
			if ((mappedMethodName.equals("channelRead0"))
					&& (method.name.equals(METHOD_CHANNEL_READ_0_NAME) && method.desc.equals(METHOD_CHANNEL_READ_0_DESCRIPTOR))) {
				LOGGER.info("[ASM] Performing injection for method \"channelRead0\"");

				InsnList insnList = new InsnList();

				// MinecraftForge.EVENT_BUS
				insnList.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
				// me.hink.tweaker.event.events.ReadPacketEvent
				insnList.add(new TypeInsnNode(NEW, "me/hink/tweaker/event/events/ReadPacketEvent"));

				insnList.add(new InsnNode(DUP));

				// (Packet p_channelRead0_2_)
				insnList.add(new VarInsnNode(ALOAD, 2));

				// MinecraftForge.EVENT_BUS.post(new ReadPacketEvent(p_channelRead0_2_))
				insnList.add(new MethodInsnNode(INVOKESPECIAL, "me/hink/tweaker/event/events/ReadPacketEvent", "<init>", isObfuscated ? "(Lid;)V" : "(Lnet/minecraft/network/Packet;)V", false));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));

				LabelNode label = new LabelNode();

				// if (MinecraftForge.EVENT_BUS.post(new ReadPacketEvent(p_channelRead0_2_))) { return; }
				insnList.add(new JumpInsnNode(IFEQ, label));
				insnList.add(new InsnNode(RETURN));

				insnList.add(label);

				method.instructions.insertBefore(method.instructions.getFirst(), insnList);
			}
		}

		// TODO
	}

	/**
	 * This method provides the ASM class transformer routine for
	 * Vanilla Minecraft class {@see net.minecraft.client.network.NetHandlerPlayClient}
	 *
	 * <p>1.8.9 <a href="http://export.mcpbot.bspk.rs/stable/1.8.9/">stable_22</a> MCP mappings</p>
	 * <table>
	 *     <tr>
	 *         <th title="Package Name">Pkg Name</th>
	 *         <th title="Searge Name">Srg Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="net.minecraft.network">net/minecraft/client/network</th>
	 *         <th>NetHandlerPlayClient</th>
	 *         <th>bcy</th>
	 *     </tr>
	 * </table>
	 *
	 * <hr>
	 *
	 * <p>net.minecraft.client.network.NetHandlerPlayClient#addToSendQueue(net.minecraft.network.Packet)</p>
	 * <table>
	 *     <tr>
	 *         <th title="MCP Name">MCP Name</th>
	 *         <th title="Searge Name">SRG Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *         <th title="Searge Descriptor (Unobfuscated)">SRG Descriptor (Unobfuscated)</th>
	 *         <th title="Searge Descriptor (Obfuscated)">SRG Descriptor (Obfuscated)</th>
	 *     </tr>
	 *     <tr>
	 *         <th>addToSendQueue</th>
	 *         <th>func_147297_a</th>
	 *         <th>a</th>
	 *         <th>(Lnet/minecraft/network/Packet;)V</th>
	 *         <th title="(Lnet/minecraft/network/Packet;)V --> (Lff;)V">(Lff;)V</th>
	 *     </tr>
	 * </table>
	 *
	 * <pre class="code">
	 * public void addToSendQueue(Packet p_147297_1_)
	 * + if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new me.hink.tweaker.event.events.SendPacketEvent(p_147297_1_))) {
	 * +    return;
	 * + }
	 * this.netManager.sendPacket(p_147297_1_);
	 * <code class="java">
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformNetHandlerPlayClient(ClassNode classNode, boolean isObfuscated) {
		final String METHOD_ADD_TO_SEND_QUEUE_NAME = isObfuscated ? "a" : "addToSendQueue";
		// net/minecraft/network/Packet --> ff
		final String METHOD_ADD_TO_SEND_QUEUE_DESCRIPTOR = isObfuscated ? "(Lff;)V" : "(Lnet/minecraft/network/Packet;)V";

		String mappedMethodName;

		for (MethodNode method : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, method.name, method.desc);

			if ((mappedMethodName.equals("addToSendQueue") || mappedMethodName.equals("func_147297_a"))
					&& method.name.equals(METHOD_ADD_TO_SEND_QUEUE_NAME) && method.desc.equals(METHOD_ADD_TO_SEND_QUEUE_DESCRIPTOR)) {
				LOGGER.info("[ASM] Performing injection for method \"addToSendQueue\"");

				InsnList insnList = new InsnList();

				// if (MinecraftForge.EVENT_BUS.post(new SendPacketEvent(p_147297_1_)))
				insnList.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
				// new SendPacketEvent(p_147297_1_)
				insnList.add(new TypeInsnNode(NEW, "me/hink/tweaker/event/events/SendPacketEvent"));

				insnList.add(new InsnNode(DUP));

				// ... addToSendQueue(Packet p_147297_1_) { ...
				insnList.add(new VarInsnNode(ALOAD, 1));

				// MinecraftForge.EVENT_BUS.post(new SendPacketEvent(p_147297_1_));
				insnList.add(new MethodInsnNode(INVOKESPECIAL, "me/hink/tweaker/event/events/SendPacketEvent", "<init>", isObfuscated ? "(Lff;)V" : "(Lnet/minecraft/network/Packet;)V", false));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));

				LabelNode label = new LabelNode();

				// { return; }
				insnList.add(new JumpInsnNode(IFEQ, label));
				insnList.add(new InsnNode(RETURN));

				insnList.add(label);

				method.instructions.insertBefore(method.instructions.getFirst(), insnList);

				break;
			}
		}
	}

	/**
	 * {@see <a href="https://git.io/vdtG8">Forge API Patch</a>}
	 *
	 * <p>1.8.9 <a href="http://export.mcpbot.bspk.rs/stable/1.8.9/">stable_22</a> MCP mappings</p>
	 * <table>
	 *     <tr>
	 *         <th title="Package Name">Pkg Name</th>
	 *         <th title="Searge Name">Srg Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="net.minecraft.network">net/minecraft/util</th>
	 *         <th>ScreenShotHelper</th>
	 *         <th>avj</th>
	 *     </tr>
	 * </table>
	 *
	 * <hr>
	 *
	 * <p>net.minecraft.util.ScreenShotHelper#saveScreenshot(java.io.File, java.lang.String, int, int, net.minecraft.client.shader.Framebuffer)</p>
	 * <table>
	 *     <tr>
	 *         <th title="MCP Name">MCP Name</th>
	 *         <th title="Searge Name">SRG Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *         <th title="Searge Descriptor (Unobfuscated)">SRG Descriptor (Unobfuscated)</th>
	 *         <th title="Searge Descriptor (Obfuscated)">SRG Descriptor (Obfuscated)</th>
	 *     </tr>
	 *     <tr>
	 *         <th>saveScreenshot</th>
	 *         <th>func_148259_a</th>
	 *         <th>a</th>
	 *         <th>(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;</th>
	 *         <th title="(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent; --> (Ljava/io/File;Ljava/lang/String;IILbfw;)Leu;">(Ljava/io/File;Ljava/lang/String;IILbfw;)Leu;</th>
	 *     </tr>
	 * </table>
	 *
	 * <pre class="code">
	 * // net.minecraft.util.ScreenShotHelper#saveScreenshot(java.io.File, java.lang.String, int, int, net.minecraft.client.shader.Framebuffer)
	 * ...
	 * + me.hink.tweaker.event.events.ScreenShotEvent event = me.hink.tweaker.event.CustomHooksClient.onScreenShot(bufferedimage, file2);
	 * +
	 * + if (event.isCancelable())
	 * + {
	 * +    return event.geCancelMessage();
	 * + }
	 * + else
	 * + {
	 * +    file2 = event.getScreenShot();
	 * + }
	 *
	 * ImageIO.write(bufferedimage, "png", (File)file2);
	 * IChatComponent ichatcomponent = new ChatComponentText(file2.getName());
	 * ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
	 * ichatcomponent.getChatStyle().setUnderlined(Boolean.valueOf(true));
	 *
	 * return new ChatComponentTranslation("screenshot.success", new Object[] {ichatcomponent});
	 * ...
	 * </pre><code class="code">
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformScreenShotHelper(ClassNode classNode, boolean isObfuscated) {
		final String SAVE_SCREENSHOT = isObfuscated ?
				"a" : "saveScreenshot";
		// net/minecraft/client/shader/Framebuffer --> bfw
		// net/minecraft/util/IChatComponent --> eu
		final String SAVE_SCREENSHOT_DESCRIPTOR = isObfuscated ?
				"(Ljava/io/File;IILbfw;)Leu;" : "(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;";

		String mappedMethodName;

		for (MethodNode methodNode : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);

			if ((methodNode.name.equals(SAVE_SCREENSHOT) && methodNode.desc.equals(SAVE_SCREENSHOT_DESCRIPTOR))
					&& (mappedMethodName.equals("saveScreenshot") || mappedMethodName.equals("func_148259_a"))) {
				LOGGER.info("[ASM] Performing injection for method \"saveScreenshot\"");

				AbstractInsnNode targetNode = null;

				for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
//					System.out.println(abstractInsnNode.getOpcode());

					if (abstractInsnNode instanceof VarInsnNode) {
						VarInsnNode aload___7 = (VarInsnNode) abstractInsnNode;

						if (aload___7.getOpcode() == ALOAD) {
							if (abstractInsnNode.getNext() instanceof LdcInsnNode) {
								LdcInsnNode ldc___png = (LdcInsnNode) abstractInsnNode.getNext();

								if (ldc___png.getOpcode() == LDC && ldc___png.cst.toString().equals("png")) {
									LOGGER.info("[ASM] Found target node for insertion point...");

									targetNode = abstractInsnNode;

									break;
								}
							}
						}
					}
				}

				InsnList insnList = new InsnList();

				/*
				 * // 1
				 * + MinecraftForge.EVENT_BUS.post(new ScreenShotEvent(bufferedimage, file2));
				 */
//				insnList.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
//				insnList.add(new TypeInsnNode(NEW, "me/hink/tweaker/event/events/ScreenShotEvent"));
//				insnList.add(new InsnNode(DUP));
//				insnList.add(new VarInsnNode(ALOAD, 7));
//				insnList.add(new VarInsnNode(ALOAD, 8));
//				insnList.add(new MethodInsnNode(INVOKESPECIAL, "me/hink/tweaker/event/events/ScreenShotEvent", "<init>", "(Ljava/awt/image/BufferedImage;Ljava/io/File;)V", false));
//				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
//				insnList.add(new InsnNode(POP));

				/*
				 * // 2
				 * + CustomHooksClient.onScreenShot(bufferedimage, file2);
				 */
//				insnList.add(new VarInsnNode(ALOAD, 8));
//				insnList.add(new MethodInsnNode(INVOKESTATIC, "me/hink/tweaker/event/CustomHooksClient", "onScreenShot", "(Ljava/awt/image/BufferedImage;Ljava/io/File;)Lme/hink/tweaker/event/events/ScreenShotEvent;", false));
//				insnList.add(new InsnNode(POP));
//				insnList.add(new VarInsnNode(ALOAD, 7));

				/*
				 * // 2
				 * + ScreenShotEvent event = CustomHooksClient.onScreenShot(bufferedimage, file2);
				 * +
				 * + if (event.isCanceled())
				 * + {
				 * +     System.out.println("canceled event!");
				 * + }
				 */
				insnList.add(new VarInsnNode(ALOAD, 8));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "me/hink/tweaker/event/CustomHooksClient", "onScreenShot", "(Ljava/awt/image/BufferedImage;Ljava/io/File;)Lme/hink/tweaker/event/events/ScreenShotEvent;", false));
				insnList.add(new VarInsnNode(ASTORE, 9));

				insnList.add(new VarInsnNode(ALOAD, 9));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "me/hink/tweaker/event/events/ScreenShotEvent", "isCanceled", "()Z", false));

				LabelNode labelNode = new LabelNode();
				insnList.add(new JumpInsnNode(IFEQ, labelNode));

				insnList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
				insnList.add(new LdcInsnNode("canceled event!"));
				insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
				insnList.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[]{"me/hink/tweaker/event/events/ScreenShotEvent"}, 0, null));
				insnList.add(new VarInsnNode(ALOAD, 7));

				if (targetNode != null) {
					// 1
//					LOGGER.info("[ASM] Performing insertion at target node (" + targetNode.getOpcode() + ") ...");
//					methodNode.instructions.insertBefore(targetNode, insnList);

					// 2
					LOGGER.info("[ASM] Performing insertion before target node (" + targetNode.getNext().getOpcode() + ") ...");
					methodNode.instructions.insertBefore(targetNode.getNext(), insnList);
				} else {
					LOGGER.info("[ASM] Failed to find target node for insertion...");
				}

				break;
			}
		}
	}

	/**
	 * <h3>1.8.9 <a href="http://export.mcpbot.bspk.rs/stable/1.8.9/">stable_22</a> MCP mappings</h3>
	 * <table>
	 *     <tr>
	 *         <th title="Package Name">Pkg Name</th>
	 *         <th title="Searge Name">Srg Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="net.minecraft.client.renderer.entity">net/minecraft/client/renderer/entity</th>
	 *         <th>RenderPlayer</th>
	 *         <th>bln</th>
	 *     </tr>
	 * </table>
	 *
	 * <hr>
	 *
	 * <p>net.minecraft.client.renderer.entity.RenderPlayer#renderRightArm(net.minecraft.client.entity.AbstractClientPlayer)</p>
	 * <table>
	 *     <tr>
	 *         <th title="MCP Name">MCP Name</th>
	 *         <th title="Searge Name">SRG Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *         <th title="Searge Descriptor (Unobfuscated)">SRG Descriptor (Unobfuscated)</th>
	 *         <th title="Searge Descriptor (Obfuscated)">SRG Descriptor (Obfuscated)</th>
	 *     </tr>
	 *     <tr>
	 *         <th>renderRightArm</th>
	 *         <th>func_177138_b</th>
	 *         <th>b</th>
	 *         <th>(Lnet/minecraft/client/entity/AbstractClientPlayer;)V</th>
	 *         <th title="(Lnet/minecraft/client/entity/AbstractClientPlayer;)V --> (Lbet;)V">(Lbet;)V</th>
	 *     </tr>
	 * </table>
	 *
	 * <pre class="code"><code class="java">
	 * // net.minecraft.client.renderer.entity.RenderPlayer
	 * public void renderRightArm(AbstractClientPlayer clientPlayer)
	 * {
	 *     float f = 1.0F;
	 *     GlStateManager.color(f, f, f);
	 *     ModelPlayer modelplayer = this.getMainModel();
	 * 	   this.setModelVisibilities(clientPlayer);
	 * 	   modelplayer.swingProgress = 0.0F;
	 *
	 * -   modelplayer.isSneak = false;
	 * +   modelplayer.isRiding = modelplayer.isSneak = false;
	 *
	 *     modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
	 *     modelplayer.renderRightArm();
	 * }
	 * </code></pre>
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformRenderPlayer(ClassNode classNode, boolean isObfuscated) {
		LOGGER.info("[ASM] Transforming net.minecraft.client.renderer.entity.RenderPlayer");

		final String CLASS_MODEL_PLAYER = isObfuscated ? "bbr" : "net/minecraft/client/model/ModelPlayer";				// A superclass: net.minecraft.client.model.ModelPlayer

		// SRG Name: field_78117_n
		final String FIELD_IS_SNEAK = isObfuscated ? "n" : "isSneak";													// superclass IS: net.minecraft.client.model.ModelBiped / bbj
		// SRG Name: field_78093_q
		final String FIELD_IS_RIDING = isObfuscated ? "q" : "isRiding";													// superclass IS: net.minecraft.client.model.ModelBase / bbo

		final String METHOD_RENDER_RIGHT_ARM_NAME = isObfuscated ? "b" : "renderRightArm";
		// net/minecraft/client/entity/AbstractClientPlayer --> bet
		final String METHOD_RENDER_RIGHT_ARM_DESCRIPTOR = isObfuscated ? "(Lbet;)V" : "(Lnet/minecraft/client/entity/AbstractClientPlayer;)V";

		String mappedMethodName;

		for (MethodNode method : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, method.name, method.desc);

			if ((mappedMethodName.equals("renderRightArm") || mappedMethodName.equals("func_177138_b"))
					&& (method.name.equals(METHOD_RENDER_RIGHT_ARM_NAME) && method.desc.equals(METHOD_RENDER_RIGHT_ARM_DESCRIPTOR))) {
				LOGGER.info("[ASM] Performing injection for method \"renderRightArm\"");

				AbstractInsnNode targetNode = null;

				for (AbstractInsnNode insnNode : method.instructions.toArray()) {
					// ALOAD 3
					// ICONST_0
					// PUTFIELD net/minecraft/client/model/ModelPlayer.isSneak : Z // FIXME
					if ( 	insnNode.getOpcode()					== ICONST_0
							&& insnNode.getNext().getOpcode()		== PUTFIELD) {
						LOGGER.info("[ASM] Found target node for insertion point...");
						LOGGER.info("[ASM] Removing 3 instructions...");

						targetNode = insnNode;

						break;
					}
				}

				// TODO: Fix else branch
				if (targetNode != null) {
					// - modelplayer.isSneak = false;
					for (int i = 0; i < 3; i++) {
						targetNode = targetNode.getNext();
						method.instructions.remove(targetNode.getPrevious());
					}

					InsnList insnList = new InsnList();

					// if (TConfig.isRightArmRidableEntityRenderFixEnabled) {
					insnList.add(new FieldInsnNode(GETSTATIC, "me/hink/tweaker/config/TConfig", "isRightArmRidableEntityRenderFixEnabled", "Z"));

					LabelNode configFieldClause = new LabelNode();
					insnList.add(new JumpInsnNode(IFEQ, configFieldClause));

					// + modelplayer.isRiding = modelplayer.isSneak = false;
					insnList.add(new VarInsnNode(ALOAD, 3));
					insnList.add(new VarInsnNode(ALOAD, 3));
					insnList.add(new InsnNode(ICONST_0));
					insnList.add(new InsnNode(DUP_X1));

					insnList.add(new FieldInsnNode(PUTFIELD, CLASS_MODEL_PLAYER, FIELD_IS_SNEAK, "Z"));
					insnList.add(new FieldInsnNode(PUTFIELD, CLASS_MODEL_PLAYER, FIELD_IS_RIDING, "Z"));

					// Close off the frame...
					insnList.add(new LabelNode(configFieldClause.getLabel()));
					insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

					targetNode = null;

					for (AbstractInsnNode abstractInsnNode : method.instructions.toArray()) {
						if (	abstractInsnNode.getOpcode()						== ALOAD
								&& abstractInsnNode.getNext().getOpcode()			== FCONST_0
								&& abstractInsnNode.getNext().getNext().getOpcode() == FCONST_0) {
							LOGGER.info("[ASM] Found injection point...");

							targetNode = abstractInsnNode;
						}
					}

					if (targetNode != null) {
						LOGGER.info("[ASM] Performing insertion before target node...");
						method.instructions.insertBefore(targetNode, insnList);
					} else {
						LOGGER.info("[ASM] Failed to find target node for insertion...");
					}

					break;
				}
			}
		}
	}

	/**
	 * ASM class transformer method for class EntityLivingBase
	 *
	 * <h3>1.8.9 <a href="http://export.mcpbot.bspk.rs/stable/1.8.9/">stable_22</a> MCP mappings</h3>
	 * <table>
	 *     <tr>
	 *         <th title="Package Name">Pkg Name</th>
	 *         <th title="Searge Name">Srg Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="net.minecraft.entity">net/minecraft/entity</th>
	 *         <th>EntityLivingBase</th>
	 *         <th>pr</th>
	 *     </tr>
	 * </table>
	 *
	 * <hr>
	 *
	 * <p>net.minecraft.entity.EntityLivingBase#getLook(float)</p>
	 * <table>
	 *     <tr>
	 *         <th title="MCP Name">MCP Name</th>
	 *         <th title="Searge Name">SRG Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *         <th title="Searge Descriptor (Unobfuscated)">SRG Descriptor (Unobfuscated)</th>
	 *         <th title="Searge Descriptor (Obfuscated)">SRG Descriptor (Obfuscated)</th>
	 *     </tr>
	 *     <tr>
	 *         <th>getLook</th>
	 *         <th>func_70676_i</th>
	 *         <th>d</th>
	 *         <th>(F)Lnet/minecraft/util/Vec3;</th>
	 *         <th title="(F)Lnet/minecraft/util/Vec3; --> (F)Laui;">(F)Laui;</th>
	 *     </tr>
	 * </table>
	 *
	 * <ol>
	 *     <li>Patches Vanilla Minecraft bug <a href="https://bugs.mojang.com/browse/MC-67665" title="Mouse click position always lags a few frames behind the crosshair">[MC-67665]</a></li>
	 * </ol>
	 *
	 * <pre class="code"><code class="java">
	 * public Vec3 getLook(float partialTicks)
	 * {
	 * +   if (TConfig.isHeadLookYawCalculationFixEnabled && this instanceof EntityPlayerSP)
	 * +   {
	 * +       return super.getLook(partialTicks);
	 * +   }
	 *     if (partialTicks == 1.0F)
	 *     {
	 *         return this.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
	 *     }
	 *     else
	 *     {
	 *         float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
	 *         float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
	 *         return this.getVectorForRotation(f, f1);
	 *     }
	 * }
	 * </code></pre>
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformEntityLivingBase(ClassNode classNode, boolean isObfuscated) {
		final String METHOD_GET_LOOK_NAME = isObfuscated ? "d" : "getLook";
		final String METHOD_GET_LOOK_DESCRIPTOR = isObfuscated ? "(F)Laui;" : "(F)Lnet/minecraft/util/Vec3;";

		final String METHOD_GET_LOOK_MCP_NAME = "getLook";
		final String METHOD_GET_LOOK_SRG_NAME = "func_70676_i";

		String mappedMethodName;

		for (MethodNode method : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, method.name, method.desc);

			if ((mappedMethodName.equals(METHOD_GET_LOOK_MCP_NAME) || mappedMethodName.equals(METHOD_GET_LOOK_SRG_NAME))
					&& (method.name.equals(METHOD_GET_LOOK_NAME) && method.desc.equals(METHOD_GET_LOOK_DESCRIPTOR))) {
				final String classEntity = FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/entity/Entity");
				final String classEntityPlayerSP = FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/client/entity/EntityPlayerSP");

				InsnList insnList = new InsnList();

				// if (TConfig.isHeadLookYawCalculationFixEnabled && ...) {
				insnList.add(new FieldInsnNode(GETSTATIC, "me/hink/tweaker/config/TConfig", "isHeadLookYawCalculationFixEnabled", "Z"));

				LabelNode labelFirstClause = new LabelNode();
				insnList.add(new JumpInsnNode(IFEQ, labelFirstClause));

				// if (... && this instanceof EntityPlayerSP) {
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new TypeInsnNode(INSTANCEOF, classEntityPlayerSP));

				insnList.add(new JumpInsnNode(IFEQ, labelFirstClause));

				// return super.getLook(partialTicks);
				insnList.add(new VarInsnNode(ALOAD, 0));
				insnList.add(new VarInsnNode(FLOAD, 1));
				insnList.add(new MethodInsnNode(INVOKESPECIAL, classEntity, method.name, method.desc, false));
				insnList.add(new InsnNode(ARETURN));

				insnList.add(labelFirstClause);

				method.instructions.insertBefore(method.instructions.getFirst(), insnList);

				break;
			}
		}
	}

	/**
	 * This method provides the ASM class transformer routine for class EntityRenderer
	 *
	 * <h3>1.8.9 <a href="http://export.mcpbot.bspk.rs/stable/1.8.9/">stable_22</a> MCP mappings</h3>
	 * <table>
	 *     <tr>
	 *         <th title="Package Name">Pkg Name</th>
	 *         <th title="Searge Name">Srg Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="net.minecraft.client.renderer">net/minecraft/client/renderer</th>
	 *         <th>EntityRenderer</th>
	 *         <th>bfk</th>
	 *     </tr>
	 * </table>
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformEntityRenderer(ClassNode classNode, boolean isObfuscated) {
		final String METHOD_HURT_CAMERA_EFFECT_NAME = isObfuscated ? "bfk" : "hurtCameraEffect";
		final String METHOD_HURT_CAMERA_EFFECT_DESCRIPTOR = isObfuscated ? "(F)V" : "(F)V";

		final String METHOD_HURT_CAMERA_EFFECT_MCP_NAME = "hurtCameraEffect";
		final String METHOD_HURT_CAMERA_EFFECT_SRG_NAME = "func_78482_e";

		String mappedMethodName;

		for (MethodNode methodNode : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);

			if ((mappedMethodName.equals(METHOD_HURT_CAMERA_EFFECT_MCP_NAME) || mappedMethodName.equals(METHOD_HURT_CAMERA_EFFECT_SRG_NAME))
					&& (methodNode.name.equals(METHOD_HURT_CAMERA_EFFECT_NAME) && methodNode.desc.equals(METHOD_HURT_CAMERA_EFFECT_DESCRIPTOR))) {
				LOGGER.info("[ASM] Performing injection for method \"hurtCameraEffect\"");

				InsnList insnList = new InsnList();

				// if (!isHurtCameraEffectEnabled)
				insnList.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(TConfig.class), "isHurtCameraEffectEnabled", "Z"));

				LabelNode l2 = new LabelNode();
				insnList.add(new JumpInsnNode(IFNE, l2));

				// return;
				insnList.add(new InsnNode(RETURN));

				insnList.add(l2);

				methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), insnList);

				break;
			}
		}
	}

	/**
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformGuiContainer(ClassNode classNode, boolean isObfuscated) {
		final String METHOD_MOUSE_CLICKED_NAME = isObfuscated ? "a" : "mouseClicked";
		final String METHOD_MOUSE_CLICKED_DESCRIPTOR = isObfuscated ? "(III)V" : "(III)V";

		final String METHOD_MOUSE_CLICKED_MCP_NAME = "";
		final String METHOD_MOUSE_CLICKED_SRG_NAME = "";

		String mappedMethodName;

		for (MethodNode methodNode : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);

			if ((mappedMethodName.equals(METHOD_MOUSE_CLICKED_MCP_NAME) || mappedMethodName.equals(METHOD_MOUSE_CLICKED_SRG_NAME))
					&& (methodNode.name.equals(METHOD_MOUSE_CLICKED_NAME) && methodNode.desc.equals(METHOD_MOUSE_CLICKED_DESCRIPTOR))) {
				AbstractInsnNode targetNode = null;

				for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
					if (insnNode.getOpcode() == LDC &&
						insnNode.getNext().getOpcode() == LCMP &&
						insnNode.getPrevious().getOpcode() == LSUB) {

						targetNode = insnNode;

						break;
					}
				}

				if (targetNode != null) {
					InsnList injection = new InsnList();

					injection.add(new VarInsnNode(ALOAD, 0));
					injection.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/gui/inventory/GuiContainer", "doubleClick", "Z"));
				}

				break;
			}
		}
	}

	/**
	 * This method provides the ASM class transformer routine for class {@see Minecraft}
	 *
	 * <h3>1.8.9 <a href="http://export.mcpbot.bspk.rs/stable/1.8.9/">stable_22</a> MCP mappings</h3>
	 * <table>
	 *     <tr>
	 *         <th title="Package Name">Pkg Name</th>
	 *         <th title="Searge Name">Srg Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="net.minecraft.client">net/minecraft/client</th>
	 *         <th>Minecraft</th>
	 *         <th>ave</th>
	 *     </tr>
	 * </table>
	 * <hr>
	 * <p>net.minecraft.client.Minecraft#toggleFullscreen()</p>
	 * <table>
	 *     <tr>
	 *         <th title="MCP Name">MCP Name</th>
	 *         <th title="Searge Name">SRG Name</th>
	 *         <th title="Obfusctaed Name">Obf Name</th>
	 *         <th title="Searge Descriptor (Unobfuscated)">SRG Descriptor (Unobfuscated)</th>
	 *         <th title="Searge Descriptor (Obfuscated)">SRG Descriptor (Obfuscated)</th>
	 *     </tr>
	 *     <tr>
	 *         <th title="Toggles fullscreen mode.">toggleFullscreen</th>
	 *         <th title="Toggles fullscreen mode.">func_71352_k</th>
	 *         <th title="Toggles fullscreen mode.">q</th>
	 *         <th>()V</th>
	 *         <th title="()V <--> ()V">()V</th>
	 *     </tr>
	 * </table>
	 *
	 * <pre class="code"><code class="java">
	 * + if (TConfig.isToggleFullscreenResizeFixEnabled)
	 * + {
	 * +     Display.setResizable(false);
	 * +     Display.setResizable(true);
	 * + }
	 * </code></pre>
	 *
	 * @param classNode The ASM class node to perform bytecode manipulation on.
	 * @param isObfuscated Whether or not the class correlates to the name mappings of an unobfuscated (development)
	 * or an obfuscated (production) environment.
	 */
	private static void transformMinecraft(ClassNode classNode, boolean isObfuscated) {
		final String TOGGLE_FULLSCREEN = isObfuscated ? "q" : "toggleFullscreen";
		final String TOGGLE_FULLSCREEN_DESCRIPTOR = isObfuscated ? "()V" : "()V";

		final String METHOD_MCP_NAME = "toggleFullscreen";
		final String METHOD_SRG_NAME = "func_71352_k";

		String mappedMethodName;

		for (MethodNode methodNode : classNode.methods) {
			mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);

			if ((mappedMethodName.equals(METHOD_MCP_NAME) || mappedMethodName.equals(METHOD_SRG_NAME)) &&
					(methodNode.name.equals(TOGGLE_FULLSCREEN) && methodNode.desc.equals(TOGGLE_FULLSCREEN_DESCRIPTOR))) {
				LOGGER.info("[ASM] " + "Found method \"" + METHOD_MCP_NAME + '\"');

				MethodInsnNode methodInsnNode = null;

				for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
					if ((abstractInsnNode instanceof MethodInsnNode)) {
						methodInsnNode = (MethodInsnNode)abstractInsnNode;

						if ((methodInsnNode.owner.equals("org/lwjgl/opengl/Display"))
							&& (methodInsnNode.name.equals("setFullscreen"))
							&& (methodInsnNode.desc.equals("(Z)V"))) {
							LOGGER.info("Found injection point for fullscreen");

							break;
						}

						methodInsnNode = null;
					}
				}

				if (methodInsnNode == null) {
					LOGGER.warn("[ASM] " + "Unable to find target instruction node for injection point...");
					LOGGER.warn("[ASM] " + "Breaking to cancel transformer method.");

					break;
				}

				InsnList insnList = new InsnList();

				// TODO: Add a conditional hook

				insnList.add(new InsnNode(ICONST_0));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/Display", "setResizable", "(Z)V", false));
				insnList.add(new InsnNode(ICONST_1));
				insnList.add(new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/Display", "setResizable", "(Z)V", false));


				methodNode.instructions.insert(methodInsnNode, insnList);

				break;
			}
		}
	}
}