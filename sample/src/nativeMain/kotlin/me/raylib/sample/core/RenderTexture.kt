package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Colors.BLACK
import raylib.core.Colors.MAROON
import raylib.core.Colors.RED
import raylib.core.Colors.SKYBLUE
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.loadRenderTexture
import raylib.core.nativeStateOf
import raylib.core.textureDrawScope

fun ComponentRegistry.renderTexture() {
    component("ballMovement") {
        val renderTextureWidth = 300
        val renderTextureHeight = 300
        val loadedTexture = loadRenderTexture(renderTextureWidth, renderTextureHeight)
        val ballPosition: Vector2 by nativeStateOf {
            alloc {
                x = renderTextureWidth / 2.0f; y = renderTextureHeight / 2.0f
            }
        }
        val ballSpeed by nativeStateOf { alloc<Vector2> { x = 5.0f; y = 4.0f } }
        val ballRadius = 20
        var rotation = 0.0f

        onUpdate {
            ballPosition.x += ballSpeed.x
            ballPosition.y += ballSpeed.y

            // Check walls collision for bouncing
            if ((ballPosition.x >= (renderTextureWidth - ballRadius)) || (ballPosition.x <= ballRadius)) ballSpeed.x *= -1.0f;
            if ((ballPosition.y >= (renderTextureHeight - ballRadius)) || (ballPosition.y <= ballRadius)) ballSpeed.y *= -1.0f;

            rotation += 0.5f;
        }

        onDraw {
            fun fillTexture() = textureDrawScope(loadedTexture, SKYBLUE) {
                drawRectangle(0, 0, 20, 20, RED)
                drawCircle(ballPosition.readValue(), ballRadius.toFloat(), MAROON)
            }

            val sourceRectangle = loadedTexture.useContents {
                Rectangle(0f, 0f, texture.width.toFloat(), -texture.height.toFloat())
            }

            val dstRectangle = loadedTexture.useContents {
                Rectangle(
                    screenWidth / 2f,
                    screenHeight / 2f,
                    texture.width.toFloat(),
                    texture.height.toFloat()
                )
            }
            val origin = loadedTexture.useContents {
                Vector2(texture.width / 2f, texture.height / 2f)
            }

            drawTexture(
                fillTexture().useContents { texture.readValue() },
                sourceRectangle,
                dstRectangle,
                origin,
                WHITE,
                rotation,
            )

            drawText("DRAWING BOUNCING BALL INSIDE RENDER TEXTURE!", 10, screenHeight - 40, 20, BLACK)
            drawFPS(10, 10)
        }
    }
}