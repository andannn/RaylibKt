package me.raylib.sample.textures

import kotlinx.cinterop.useContents
import raylib.core.Colors.GRAY
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.component
import raylib.core.loadTexture
import raylib.core.remember

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
