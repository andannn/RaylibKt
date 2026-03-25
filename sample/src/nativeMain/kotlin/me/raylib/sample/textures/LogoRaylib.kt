package me.raylib.sample.textures

import io.github.andannn.raylib.assets.fileResourceResolver
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.remember

fun ComponentRegistry.logoRaylib() {
    component("logo") {
        val texture = remember {
            fileResourceResolver.resolveImageTexture("resources/raylib_logo.png")
        }
        draw {
            val (width, height) = texture.useContents { width to height }
            drawTexture(texture, screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, WHITE)
            drawText("this IS a texture!", 360, 370, 10f, GRAY)
        }
    }
}
