package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.foundation.Colors.SKYBLUE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.RectangleAlloc
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.components.fixSizedTextureComponent
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth

private const val renderTextureWidth = 300
private const val renderTextureHeight = 300

fun ComponentRegistry.renderTexture() {
    component("ballMovement") {
        val rotation = remember {
            mutableStateOf(0.0f)
        }
        val dst by remember {
            nativeStateOf {
                RectangleAlloc(
                    screenWidth / 2f,
                    screenHeight / 2f,
                    renderTextureWidth.toFloat(),
                    renderTextureWidth.toFloat(),
                )
            }
        }

        fixSizedTextureComponent(
            key = "",
            width = renderTextureWidth,
            height = renderTextureHeight,
            dstRectangle = dst,
            rotation = rotation,
            origin = Vector2(renderTextureWidth / 2f, renderTextureHeight / 2f),
            backgroundColor = SKYBLUE
        ) { texture ->
            val (textureWidth, textureHeight) = texture.useContents { width to height }
            bouncingBallContent(rectWidth = textureWidth, rectHeight = textureHeight)
        }

        update {
            rotation.value += 0.5f;
        }

        draw {
            drawText("DRAWING BOUNCING BALL INSIDE RENDER TEXTURE!", 10, screenHeight - 40, 20f, BLACK)
            drawFPS(10, 10)
        }
    }
}

private const val ballRadius = 20
private fun ComponentRegistry.bouncingBallContent(
    rectWidth: Int, rectHeight: Int
) {
    component("content") {

        val ballSpeed by remember {
            nativeStateOf { alloc<Vector2> { x = 5.0f; y = 4.0f } }
        }
        val ballPosition by remember {
            nativeStateOf {
                alloc<Vector2> {
                    x = rectWidth / 2.0f; y = rectHeight / 2.0f
                }
            }
        }

        update {
            ballPosition.x += ballSpeed.x
            ballPosition.y += ballSpeed.y

            // Check walls collision for bouncing
            if ((ballPosition.x >= (rectWidth - ballRadius)) || (ballPosition.x <= ballRadius)) ballSpeed.x *= -1.0f;
            if ((ballPosition.y >= (rectHeight - ballRadius)) || (ballPosition.y <= ballRadius)) ballSpeed.y *= -1.0f;
        }

        draw {
            drawRectangle(0, 0, 20, 20, RED)
            drawCircle(ballPosition.readValue(), ballRadius.toFloat(), MAROON)
        }
    }
}