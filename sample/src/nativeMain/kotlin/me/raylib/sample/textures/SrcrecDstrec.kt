package me.raylib.sample.textures

import kotlinx.cinterop.useContents
import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.WHITE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.components.AssetManager
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.srcrecDstrec() {
    component("srcrec_dstrec") {
        val scarfy = remember {
            find<AssetManager>().getTexture("resources/scarfy.png")
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