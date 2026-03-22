package me.raylib.sample.textures

import kotlinx.cinterop.useContents
import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.components.fileTextureAsset
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.setValue

fun ComponentRegistry.srcrecDstrec() {
    component("srcrec_dstrec") {
        val scarfy = remember {
            fileTextureAsset("resources/scarfy.png")
        }
        val (frameWidth, frameHeight) = scarfy.useContents { width.div(6f) to height }
        val sourceRec = Rectangle(0.0f, 0.0f, frameWidth, frameHeight.toFloat())
        val destRec = Rectangle(screenWidth / 2.0f, screenHeight / 2.0f, frameWidth * 2.0f, frameHeight * 2.0f)
        val origin = Vector2(frameWidth, frameHeight.toFloat())
        var rotation by remember {
            mutableStateOf(0f)
        }

        update { rotation++; }

        draw {
            drawTexture(scarfy, sourceRec, destRec, origin, WHITE, rotation)

            val (destRecX, destRecY) = destRec.useContents { x.toInt() to y.toInt() }
            drawLine(destRecX, 0, destRecX, screenHeight, GRAY)
            drawLine(0, destRecY, screenWidth, destRecY, GRAY)
            drawText("(c) Scarfy sprite by Eiden Marsal", screenWidth - 200, screenHeight - 20, 10, GRAY)
        }
    }
}