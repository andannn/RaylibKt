package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.foundation.Camera2D
import io.github.andannn.raylib.foundation.Colors.BLUE
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.Colors.RAYWHITE
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.components.fixSizedTextureComponent
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.foundation.mode2d
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.setOffset
import io.github.andannn.raylib.foundation.setTarget
import io.github.andannn.raylib.foundation.RectangleAlloc
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth

private const val PLAYER_SIZE = 40

fun ComponentRegistry.twoDCameraSplitScreen() {
    component("components") {
        val player1 by remember {
            RectangleAlloc(
                width = PLAYER_SIZE.toFloat(),
                height = PLAYER_SIZE.toFloat(),
                x = 200.0f,
                y = 200.0f,
            )
        }
        val player2 by remember {
            nativeStateOf {
                alloc<Rectangle> {
                    width = PLAYER_SIZE.toFloat()
                    height = PLAYER_SIZE.toFloat()
                    x = 200.0f
                    y = 200.0f
                }
            }
        }
        val camera1 by remember {
            nativeStateOf {
                alloc<Camera2D> {
                    setTarget(player1.x, player1.y)
                    setOffset(200.0f, 200.0f)
                    rotation = 0.0f
                    zoom = 1.0f
                }
            }
        }
        val camera2 by remember {
            nativeStateOf {
                alloc<Camera2D> {
                    setTarget(player2.x, player2.y)
                    setOffset(200.0f, 200.0f)
                    rotation = 0.0f
                    zoom = 1.0f
                }
            }
        }

        val rect1 by remember {
            RectangleAlloc(0f, 0f, screenWidth / 2f, screenHeight.toFloat())
        }

        val rect2 by remember {
            RectangleAlloc(screenWidth / 2f, 0f, screenWidth / 2f, screenHeight.toFloat())
        }

        update {
            if (KeyboardKey.KEY_S.isDown()) player1.y += 3.0f
            if (KeyboardKey.KEY_W.isDown()) player1.y -= 3.0f
            if (KeyboardKey.KEY_D.isDown()) player1.x += 3.0f
            if (KeyboardKey.KEY_A.isDown()) player1.x -= 3.0f

            if (KeyboardKey.KEY_UP.isDown()) player2.y -= 3.0f
            if (KeyboardKey.KEY_DOWN.isDown()) player2.y += 3.0f
            if (KeyboardKey.KEY_LEFT.isDown()) player2.x -= 3.0f
            if (KeyboardKey.KEY_RIGHT.isDown()) player2.x += 3.0f

            camera1.setTarget(player1.x, player1.y)
            camera2.setTarget(player2.x, player2.y)
        }

        fixSizedTextureComponent(
            key = "player1",
            width = screenWidth / 2,
            height = screenHeight,
            dstRectangle = rect1,
            backgroundColor = RAYWHITE
        ) {
            playerAndWorldComponent(camera1, player1, player2)
        }

        fixSizedTextureComponent(
            key = "player2",
            width = screenWidth / 2,
            height = screenHeight,
            dstRectangle = rect2,
            backgroundColor = RAYWHITE
        ) {
            playerAndWorldComponent(camera2, player1, player2)
        }

        component("frame") {
            draw {
                drawRectangle(screenWidth / 2 - 2, 0, 4, screenHeight, RED)
            }
        }
    }
}

private fun ComponentRegistry.playerAndWorldComponent(
    camera2D: Camera2D,
    player1: Rectangle,
    player2: Rectangle,
) = component("player") {
    draw {
        mode2d(camera2D) {
            val horizontalCount = screenWidth.div(PLAYER_SIZE) + 1
            repeat(horizontalCount) { i ->
                drawLine(PLAYER_SIZE * i, 0, PLAYER_SIZE * i, screenHeight, LIGHTGRAY)
            }
            val verticalCount = screenHeight.div(PLAYER_SIZE) + 1
            repeat(verticalCount) { i ->
                drawLine(0, PLAYER_SIZE * i, screenWidth, PLAYER_SIZE * i, LIGHTGRAY)
            }

            for (i in 0..<screenWidth / PLAYER_SIZE) {
                for (j in 0..<screenHeight / PLAYER_SIZE) {
                    drawText(
                        "[$i, $j]",
                        10 + PLAYER_SIZE * i,
                        15 + PLAYER_SIZE * j,
                        10,
                        LIGHTGRAY
                    )
                }
            }
            drawRectangle(player1.readValue(), RED)
            drawRectangle(player2.readValue(), BLUE)
        }
    }
}