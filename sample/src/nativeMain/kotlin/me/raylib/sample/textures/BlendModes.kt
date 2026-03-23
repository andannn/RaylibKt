package me.raylib.sample.textures

import io.github.andannn.raylib.foundation.BlendMode
import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.blendMode
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.loadTextureFromImage
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.foundation.useImage
import io.github.andannn.raylib.runtime.*
import kotlinx.cinterop.useContents

fun ComponentRegistry.blendModes() = component("blend modes") {
    val bgTexture = remember {
        useImage("resources/cyberpunk_street_background.png") {
            loadTextureFromImage(it)
        }
    }

    val fgTexture = remember {
        useImage("resources/cyberpunk_street_foreground.png") {
            loadTextureFromImage(it)
        }
    }

    var blendMode by remember {
        mutableStateOf(BlendMode.BLEND_ADDITIVE)
    }

    update {
        if (KeyboardKey.KEY_SPACE.isPressed()) {
            val options = BlendMode.entries
            val nextIndex = (options.indexOf(blendMode) + 1) % options.size
            blendMode = options[nextIndex]
        }
    }

    draw {
        bgTexture.useContents {
            drawTexture(bgTexture, screenWidth/2 - width/2, screenHeight/2 - height/2, WHITE)
        }

        blendMode(blendMode) {
            fgTexture.useContents {
                drawTexture(fgTexture, screenWidth/2 - width/2, screenHeight/2 - height/2, WHITE);
            }
        }

        drawText("Press SPACE to change blend modes.", 310, 350, 10f, GRAY)

        drawText("Current: $blendMode", (screenWidth / 2) - 60, 370, 10f, GRAY)

        drawText(
            "(c) Cyberpunk Street Environment by Luis Zuno (@ansimuz)",
            screenWidth - 330,
            screenHeight - 20,
            10f,
            GRAY
        )
    }
}