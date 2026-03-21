package me.raylib.sample.rres

import io.github.andannn.raylib.base.Colors.BLUE
import io.github.andannn.raylib.base.Colors.WHITE
import io.github.andannn.raylib.components.rresTextAsset
import io.github.andannn.raylib.components.rresTextureAsset
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.useContents
import rres.resources.app.AppRes

fun ComponentRegistry.readChunkData() = component("blend modes") {
    val catImageTexture = remember {
        rresTextureAsset(AppRes.rresFile, AppRes.image.scarfy_png)
    }

    val text = remember {
        rresTextAsset(AppRes.rresFile, AppRes.text.tiled_test_tmj)
    }

    draw {
        catImageTexture.useContents {
            drawTexture(catImageTexture, screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, WHITE)
        }

        drawText(text, 0, 0, 10, color = BLUE)
    }
}
