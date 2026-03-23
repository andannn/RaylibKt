package me.raylib.sample.rres

import io.github.andannn.raylib.assets.rresSoundAsset
import io.github.andannn.raylib.foundation.Colors.BLUE
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.assets.rresTextAsset
import io.github.andannn.raylib.assets.rresTextureAsset
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import kotlinx.cinterop.useContents
import rres.resources.app.AppRes

fun ComponentRegistry.readChunkData() = component("blend modes") {
    val catImageTexture = remember {
        rresTextureAsset(AppRes.rresFile, AppRes.image.scarfy_png)
    }

    val text = remember {
        rresTextAsset(AppRes.rresFile, AppRes.text.tiled_test_tmj)
    }

    val sound = remember {
        rresSoundAsset(AppRes.rresFile, AppRes.wave.sound_wav)
    }

    update {
        if (KeyboardKey.KEY_SPACE.isPressed()) {
            playSound(sound)
        }
    }

    draw {
        catImageTexture.useContents {
            drawTexture(catImageTexture, screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, WHITE)
        }

        drawText(text, 0, 0, 10, color = BLUE)
    }
}
