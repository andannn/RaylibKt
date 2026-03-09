package me.raylib.sample.textures

import kotlinx.cinterop.useContents
import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.WHITE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.loadTexture
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.remember

fun ComponentRegistry.logoRaylib() {
    component("logo") {
        val texture = remember {
            loadTexture("resources/raylib_logo.png")
        }
        onDraw {
            val (width, height) = texture.useContents { width to height }
            drawTexture(texture, screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, WHITE)
            drawText("this IS a texture!", 360, 370, 10, GRAY)
        }
    }
}
