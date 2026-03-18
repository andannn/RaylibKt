package me.raylib.sample.textures

import kotlinx.cinterop.useContents
import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.WHITE
import io.github.andannn.raylib.components.AssetManager
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.remember

fun ComponentRegistry.logoRaylib() {
    component("logo") {
        val texture = remember {
            find<AssetManager>().getTexture("resources/raylib_logo.png")
        }
        draw {
            val (width, height) = texture.useContents { width to height }
            drawTexture(texture, screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, WHITE)
            drawText("this IS a texture!", 360, 370, 10, GRAY)
        }
    }
}
