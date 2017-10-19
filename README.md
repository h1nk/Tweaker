
<div align="center">

# Tweaker (WIP)

[![Total Github Release Downloads](https://img.shields.io/github/downloads/h1nk/Tweaker/total.svg?style=flat-square)](https://github.com/h1nk/Tweaker/releases)
[![License MIT](https://img.shields.io/github/license/mashape/apistatus.svg?style=flat-square)](https://github.com/h1nk/Tweaker/blob/master/LICENSE.md)
[![Build Status](https://img.shields.io/travis/h1nk/Tweaker.svg?style=flat-square)](https://travis-ci.org/h1nk/Tweaker)

<!-- [![CursForge Downloads](http://cf.way2muchnoise.eu/full_tweaker_downloads.svg)](https://minecraft.curseforge.com/projects/tweaker/files/latest)
[![Available game version downloads on CursForge](http://cf.way2muchnoise.eu/versions/Available%20for%20Minecraft_tweaker_all.svg)](https://minecraft.curseforge.com/projects/tweaker/files/latest)
-->

[![Discord](https://img.shields.io/discord/246655054617116674.svg?style=flat-square)](https://hink.me/Discord)
[![Twitter](https://img.shields.io/twitter/follow/hinkshandle.svg?style=social&label=Follow)](https://twitter.com/hinkshandle)

Tweaker is a Minecraft Forge mod that provides tons of general enhancements, bug fixes, and core game tweak features.

</div>

---

## Build Instructions

[gradle](https://gradle.org) is required to build this mod!

You can use the gradle wrapper shipped in the Minecraft Forge MDK [here](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.8.9.html), or alternatively you may use a system-wide install of gradle which you can get and install from [here](https://gradle.org/install).

### 1. Get the project repository source

First you must get a copy of the mod source

- From [GitHub](https://github.com/h1nk/Tweaker) using [git](https://git-scm.com)...
	- Via git over HTTPS ```git clone -b "1.8.9" https://github.com/h1nk/Tweaker.git tweaker```
	- Via git over SSH ```yes | git clone -b "1.8.9" git@github.com:h1nk/Tweaker.git tweaker```
	- You can alternatively get the source from the pre-packaged master branch archive off of GitHub
		- with [libcurl](https://curl.haxx.se/libcurl): ```curl -o tweaker.zip -O https://github.com/h1nk/h1nk.github.io/archive/master.zip```
		- or with [wget](https://www.gnu.org/software/wget)... ```wget -o tweaker.zip https://github.com/h1nk/Tweaker/archive/master.zip```
		- after you download the zip file, you must extract it into a directory: ```unzip master.zip tweaker```

### 2. Change your current working directory to that of the source

Simply... ```cd ./tweaker```

### 3. Run the gradle build task

Now you can build the mod.

If you don't have gradle installed on your system and would prefer use the portable gradle wrapper as provided in the Minecraft Forge MDK you can do the following:

- Download the MDK package that correlates to the Minecraft version of the source branch you downloaded earlier
	- with curl: ```curl -O https://files.minecraftforge.net/maven/net/minecraftforge/forge/1.8.9-11.15.1.2318-1.8.9/forge-1.8.9-11.15.1.2318-1.8.9-mdk.zip```
	- with wget: ```wget https://files.minecraftforge.net/maven/net/minecraftforge/forge/1.8.9-11.15.1.2318-1.8.9/forge-1.8.9-11.15.1.2318-1.8.9-mdk.zip```
- unzip the gradle wrapper directory and scripts from the zip archive: ```unzip forge*.zip "gradlew*" "gradle/wrapper/*" && rm forge*.zip```
- set the the executable bit for the gradle wrapper binaries and scripts: ```chmod +x gradlew*```
- and finally build with the gradle wrapper script: ```./gradlew build --stacktrace --info```

---

## Features

TODO

## GUI Modifications

TODO

## Better audio options

TODO

---

## Vanilla Bug Fixes

### [MC-92216](https://bugs.mojang.com/browse/MC-92216) "Leaving bed doesn't work sometimes"

TODO

### [MC-2835](https://bugs.mojang.com/browse/MC-2835) "Resizing the Minecraft window while dead grays out the Respawn/Exit to Menu buttons and several other screens"

TODO

### [MC-67665](https://bugs.mojang.com/browse/MC-67665) "Mouse click position always lags a few frames behind the crosshair"

TODO

### [MC-1349](https://bugs.mojang.com/browse/MC-1349) "While riding a pig, horse or minecart and using F5, the hand of your character is misplaced"

This bug is a minor visual render glitch that occurs with the first person perspective of the player model. The bug affects Minecraft versions 1.4.2-1.8.8+.
The bug is caused by incorrect logic to reset the hand after the player enters a ridable entity such as a boat, horse, minecart, etc.

<div align="center">

![MC-1349 bug comparision](https://i.imgur.com/2ohUHka.png "MC-1349")

</div>

<div align="center">

### The Bug

The bug as introduced in an unknown Minecraft version:

</div>

```Java
public void renderRightArm(AbstractClientPlayer clientPlayer)
{
	float f = 1.0F;
	GlStateManager.color(f, f, f);
	ModelPlayer modelplayer = this.getMainModel();
	this.setModelVisibilities(clientPlayer);
	modelplayer.swingProgress = 0.0F;
	modelplayer.isSneak = false; // the bug resides in the exuction of this statement
	modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
	modelplayer.renderRightArm();
}
```

<div align="center">

### Mojang's Fix

Mojang's fix in 15w44b (the thirty-eighth snapshot for Minecraft version 1.9).

</div>

```Java
public void renderRightArm(AbstractClientPlayer clientPlayer)
{
	float f = 1.0F;
	GlStateManager.color(f, f, f);
	float f1 = 0.0625F;
	ModelPlayer modelplayer = this.getMainModel();
	this.setModelVisibilities(clientPlayer);
	GlStateManager.enableBlend();
	modelplayer.swingProgress = 0.0F;
	modelplayer.isSneak = false;
	modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
	modelplayer.bipedRightArm.rotateAngleX = 0.0F;
	modelplayer.bipedRightArm.render(0.0625F);
	modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
	modelplayer.bipedRightArmwear.render(0.0625F);
	GlStateManager.disableBlend();
}
```

<div align="center">

### My Fix

My fix replaces the following statement doing a proper reset of the player model.

</div>

```diff
public void renderRightArm(AbstractClientPlayer clientPlayer)
{
	float f = 1.0F;
	GlStateManager.color(f, f, f);
	ModelPlayer modelplayer = this.getMainModel();
	this.setModelVisibilities(clientPlayer);
	modelplayer.swingProgress = 0.0F;
-	modelplayer.isSneak = false;
+	if (TConfig.isRightArmRidableEntityRenderFixEnabled)
+	{
+		modelplayer.isRiding = modelplayer.isSneak = false;
+	}
+	else
+	{
+		modelplayer.isSneak = false;
+	}
	modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
	modelplayer.renderRightArm();
}
```

### [MC-417](https://bugs.mojang.com/browse/MC-417) "Arrows first bounce back then appear at correct location"

TODO

### [MC-117793](https://bugs.mojang.com/browse/MC-117793) "When creating a screenshot the clickable link in chat doesn't work" & [MC-113208](https://bugs.mojang.com/browse/MC-113208) "gameDir containing dots breaks some file opening methods"

TODO

### [MC-68754](https://bugs.mojang.com/browse/MC-68754) "Exiting fullscreen disables window resize" & [MC-111254](https://bugs.mojang.com/browse/MC-111254) "Fullscreen Disables Window Resize"

The following bug occurs when a player exits fullscreen mode. When exiting from fullscreen mode to windowed mode the game window does not allow resizing anymore. This is a bug specific to Windows users. This bug remains unpatched, despite the Mojang bug tracker ticket being "resolved".

<div align="center">

![MC-68754 bug comparison](https://i.imgur.com/SowAOms.png "MC-68754 & MC-111254")

</div>

<div align="center">

### The Bug

</div>

```Java
// net.minecraft.client.Minecraft#toggleFullscreen

/**
 * Toggles fullscreen mode.
 */
public void toggleFullscreen()
{
	try
	{
		this.fullscreen = !this.fullscreen;
		this.gameSettings.fullScreen = this.fullscreen;

		if (this.fullscreen)
		{
			this.updateDisplayMode();
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();

			if (this.displayWidth <= 0)
			{
				this.displayWidth = 1;
			}

			if (this.displayHeight <= 0)
			{
				this.displayHeight = 1;
			}
		}
		else
		{
			Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
			this.displayWidth = this.tempDisplayWidth;
			this.displayHeight = this.tempDisplayHeight;

			if (this.displayWidth <= 0)
			{
				this.displayWidth = 1;
			}

			if (this.displayHeight <= 0)
			{
				this.displayHeight = 1;
			}
		}

		if (this.currentScreen != null)
		{
			this.resize(this.displayWidth, this.displayHeight);
		}
		else
		{
			this.updateFramebufferSize();
		}
		
		// The bug resides here, since the resizability window property is cached incorrectly we need to do a manual redundant reset

		Display.setFullscreen(this.fullscreen);
		Display.setVSyncEnabled(this.gameSettings.enableVsync);
		this.updateDisplay();
	}
	catch (Exception exception)
	{
		logger.error((String)"Couldn\'t toggle fullscreen", (Throwable)exception);
	}
}
```

<div align="center">

### My Fix

</div>

```diff
// net.minecraft.client.Minecraft#toggleFullscreen

/**
 * Toggles fullscreen mode.
 */
public void toggleFullscreen()
{
	try
	{
		this.fullscreen = !this.fullscreen;
		this.gameSettings.fullScreen = this.fullscreen;

		if (this.fullscreen)
		{
			this.updateDisplayMode();
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();

			if (this.displayWidth <= 0)
			{
				this.displayWidth = 1;
			}

			if (this.displayHeight <= 0)
			{
				this.displayHeight = 1;
			}
		}
		else
		{
			Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
			this.displayWidth = this.tempDisplayWidth;
			this.displayHeight = this.tempDisplayHeight;

			if (this.displayWidth <= 0)
			{
				this.displayWidth = 1;
			}

			if (this.displayHeight <= 0)
			{
				this.displayHeight = 1;
			}
		}

		if (this.currentScreen != null)
		{
			this.resize(this.displayWidth, this.displayHeight);
		}
		else
		{
			this.updateFramebufferSize();
		}
		
+		if (TConfig.isToggleFullscreenResizeFixEnabled) {
+			Display.setResizable(false);
+			Display.setResizable(true);
+		}

		Display.setFullscreen(this.fullscreen);
		Display.setVSyncEnabled(this.gameSettings.enableVsync);
		this.updateDisplay();
	}
	catch (Exception exception)
	{
		logger.error((String)"Couldn\'t toggle fullscreen", (Throwable)exception);
	}
}
```

<div align="center">

### Video explanation of the bug:

[![](https://img.youtube.com/vi/ANfvuxkLV58/0.jpg)](https://youtu.be/ANfvuxkLV58)

</div>

<div align="center">

Read more from: [MC-68754](https://bugs.mojang.com/browse/MC-68754?focusedCommentId=347914&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-347914), [MC-111254](https://bugs.mojang.com/browse/MC-111254) & [LWJGL-142](https://github.com/LWJGL/lwjgl/issues/142)

</div>