package me.raylib.sample.textures

import kotlinx.cinterop.useContents
import raylib.core.Colors.GRAY
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.getValue
import raylib.core.loadTexture
import raylib.core.mutableStateOf
import raylib.core.remember
import raylib.core.setValue

fun ComponentRegistry.srcrecDstrec() {
    component("srcrec_dstrec") {
        val scarfy = remember {
            loadTexture("resources/scarfy.png")
        }
        val (frameWidth, frameHeight) = scarfy.useContents { width.div(6f) to height }
        val sourceRec = Rectangle(0.0f, 0.0f, frameWidth, frameHeight.toFloat())
        val destRec = Rectangle(screenWidth / 2.0f, screenHeight / 2.0f, frameWidth * 2.0f, frameHeight * 2.0f)
        val origin = Vector2(frameWidth, frameHeight.toFloat())
        var rotation by remember {
            mutableStateOf(0f)
        }

        onUpdate { rotation++; }

        onDraw {
            drawTexture(scarfy, sourceRec, destRec, origin, WHITE, rotation)

            val (destRecX, destRecY) = destRec.useContents { x.toInt() to y.toInt() }
            drawLine(destRecX, 0, destRecX, screenHeight, GRAY)
            drawLine(0, destRecY, screenWidth, destRecY, GRAY)
            drawText("(c) Scarfy sprite by Eiden Marsal", screenWidth - 200, screenHeight - 20, 10, GRAY)
        }
    }
}