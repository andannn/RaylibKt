package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Colors.BLACK
import raylib.core.Colors.MAROON
import raylib.core.Colors.RED
import raylib.core.Colors.SKYBLUE
import raylib.core.ComponentRegistry
import raylib.core.RectangleAlloc
import raylib.core.RenderTexture2D
import raylib.core.Vector2
import raylib.core.Vector2Alloc
import raylib.core.components.fixSizedTextureComponent
import raylib.core.getValue
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf

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

        onUpdate {
            rotation.value += 0.5f;
        }

        onDraw {
            drawText("DRAWING BOUNCING BALL INSIDE RENDER TEXTURE!", 10, screenHeight - 40, 20, BLACK)
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

        onUpdate {
            ballPosition.x += ballSpeed.x
            ballPosition.y += ballSpeed.y

            // Check walls collision for bouncing
            if ((ballPosition.x >= (rectWidth - ballRadius)) || (ballPosition.x <= ballRadius)) ballSpeed.x *= -1.0f;
            if ((ballPosition.y >= (rectHeight - ballRadius)) || (ballPosition.y <= ballRadius)) ballSpeed.y *= -1.0f;
        }

        onDraw {
            drawRectangle(0, 0, 20, 20, RED)
            drawCircle(ballPosition.readValue(), ballRadius.toFloat(), MAROON)
        }
    }
}