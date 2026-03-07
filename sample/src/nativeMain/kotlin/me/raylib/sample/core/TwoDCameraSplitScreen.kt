package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Camera2D
import raylib.core.Colors.BLUE
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.RAYWHITE
import raylib.core.Colors.RED
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.Rectangle
import raylib.core.RectangleAlloc
import raylib.core.component
import raylib.core.components.fixSizedTextureComponent
import raylib.core.getValue
import raylib.core.mode2d
import raylib.core.nativeStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.remember
import raylib.core.setOffset
import raylib.core.setTarget

private const val PLAYER_SIZE = 40

fun ComponentRegistry.twoDCameraSplitScreen() {
    component("components") {
        val player1 by remember {
            nativeStateOf {
                alloc<Rectangle> {
                    width = PLAYER_SIZE.toFloat()
                    height = PLAYER_SIZE.toFloat()
                    x = 200.0f
                    y = 200.0f
                }
            }
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
            nativeStateOf { RectangleAlloc(0f, 0f, screenWidth / 2f, screenHeight.toFloat()) }
        }

        val rect2 by remember {
            nativeStateOf { RectangleAlloc(screenWidth / 2f, 0f, screenWidth / 2f, screenHeight.toFloat()) }
        }

        onUpdate {
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
            tag = "player1",
            width = screenWidth / 2,
            height = screenHeight,
            dstRectangle = rect1,
            backgroundColor = RAYWHITE
        ) {
            playerAndWorldComponent(camera1, player1, player2)
        }

        fixSizedTextureComponent(
            tag = "player2",
            width = screenWidth / 2,
            height = screenHeight,
            dstRectangle = rect2,
            backgroundColor = RAYWHITE
        ) {
            playerAndWorldComponent(camera2, player1, player2)
        }

        component("frame") {
            onDraw {
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
    onDraw {
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