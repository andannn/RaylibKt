package me.raylib.sample.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Camera2D
import raylib.core.Colors
import raylib.core.Colors.BLUE
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.RAYWHITE
import raylib.core.Colors.RED
import raylib.core.Colors.WHITE
import raylib.core.ComponentFactory
import raylib.core.DrawScope
import raylib.core.KeyboardKey
import raylib.core.Rectangle
import raylib.core.RenderTexture
import raylib.core.Vector2
import raylib.core.loadRenderTexture
import raylib.core.mode2d
import raylib.core.setOffset
import raylib.core.setTarget
import raylib.core.textureDrawScope
import raylib.core.window

private const val PLAYER_SIZE = 40

fun twoDCameraSplitScreen() {
    window(
        title = "raylib [core] example - 2d camera split screen",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.BLACK
    ) {
        componentRegistry {
            component()
        }
    }
}

private fun ComponentFactory.component() {
    component("components") {
        val player1 = alloc<Rectangle>().apply {
            width = PLAYER_SIZE.toFloat()
            height = PLAYER_SIZE.toFloat()
            x = 200.0f
            y = 200.0f
        }
        val player2 = alloc<Rectangle>().apply {
            width = PLAYER_SIZE.toFloat()
            height = PLAYER_SIZE.toFloat()
            x = 200.0f
            y = 200.0f
        }
        val camera1 = alloc<Camera2D>().apply {
            setTarget(player1.x, player1.y)
            setOffset(200.0f, 200.0f)
            rotation = 0.0f
            zoom = 1.0f
        }
        val camera2 = alloc<Camera2D>().apply {
            setTarget(player2.x, player2.y)
            setOffset(200.0f, 200.0f)
            rotation = 0.0f
            zoom = 1.0f
        }

        val screenCamera1 = loadRenderTexture(screenWidth / 2, screenHeight)
        val screenCamera2 = loadRenderTexture(screenWidth / 2, screenHeight)

        val splitScreenRect = screenCamera1.useContents {
            Rectangle(
                0.0f,
                0.0f,
                texture.width.toFloat(),
                -texture.height.toFloat()
            )
        }

        provideHandlers {
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

            onDraw {
                fun DrawScope.fillTexture(
                    texture: CValue<RenderTexture>,
                    camera: Camera2D
                ) = textureDrawScope(texture, RAYWHITE) {
                    mode2d(camera) {
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
                drawTexture(
                    fillTexture(screenCamera1, camera1).useContents { texture.readValue() },
                    splitScreenRect,
                    Vector2(0.0f, 0.0f),
                    WHITE,
                )
                drawTexture(
                    fillTexture(screenCamera2, camera2).useContents { texture.readValue() },
                    splitScreenRect,
                    Vector2(screenWidth.div(2f), 0.0f),
                    WHITE,
                )
                drawRectangle(screenWidth / 2 - 2, 0, 4, screenHeight, RED)
            }
        }
    }
}