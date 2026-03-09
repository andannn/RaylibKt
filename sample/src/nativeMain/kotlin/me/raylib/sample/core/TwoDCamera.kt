package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import platform.posix.expf
import platform.posix.logf
import io.github.andannn.raylib.base.Camera2D
import io.github.andannn.raylib.base.Color
import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.BLUE
import io.github.andannn.raylib.base.Colors.DARKGRAY
import io.github.andannn.raylib.base.Colors.GREEN
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Colors.SKYBLUE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.RectangleAlloc
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.base.mode2d
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.base.randomValue
import io.github.andannn.raylib.base.setOffset
import io.github.andannn.raylib.base.setTarget
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue
import raylib.interop.DrawLine
import raylib.interop.Fade

private const val MAX_BUILDINGS = 100
fun ComponentRegistry.twoDCamera() {
    component("key") {
        val player by remember {
            nativeStateOf { RectangleAlloc(400f, 280f, 40f, 40f) }
        }
        var spacex by remember {
            mutableStateOf(0f)
        }
        val buildings = remember {
            List(MAX_BUILDINGS) { index ->
                val width = randomValue(50, 200).toFloat()
                val height = randomValue(100, 800).toFloat()
                (Rectangle(
                    width = width,
                    height = height,
                    y = screenHeight - 130.0f - height,
                    x = -6000.0f + spacex
                ) to Color(
                    randomValue(200, 240),
                    randomValue(200, 240),
                    randomValue(200, 250),
                ))
                    .also {
                        spacex += width
                    }
            }
        }
        val camera by remember {
            nativeStateOf {
                alloc<Camera2D>().apply {
                    setTarget(player.x + 20.0f, player.y + 20.0f)
                    setOffset(screenWidth / 2.0f, screenHeight / 2.0f)
                    zoom = 1.0f
                }
            }
        }

        // update player.
        onUpdate {
            if (KeyboardKey.KEY_RIGHT.isDown()) {
                player.x += 2.0f
            } else if (KeyboardKey.KEY_LEFT.isDown()) {
                player.x -= 2.0f
            }
            camera.setTarget(player.x + 20.0f, player.y + 20.0f)

            if (KeyboardKey.KEY_A.isDown()) {
                camera.rotation = (camera.rotation - 1).coerceIn(-40f, 40f)
            } else if (KeyboardKey.KEY_S.isDown()) {
                camera.rotation = (camera.rotation + 1).coerceIn(-40f, 40f)
            }

            camera.zoom = expf(logf(camera.zoom) + (mouseWheelMove * 0.1f)).coerceIn(0.1f, 3f)

            if (KeyboardKey.KEY_R.isDown()) {
                camera.zoom = 1.0f
                camera.rotation = 0.0f
            }
        }

        onDraw {
            mode2d(camera.readValue()) {
                drawRectangle(-6000, 320, 13000, 8000, DARKGRAY)
                buildings.forEach { (buildingRect, color) ->
                    drawRectangle(buildingRect, color)
                }
                drawRectangle(player.readValue(), RED)
                drawLine(
                    camera.target.x.toInt(),
                    -screenHeight * 10,
                    camera.target.x.toInt(),
                    screenHeight * 10,
                    GREEN
                )
                DrawLine(
                    -screenWidth * 10,
                    camera.target.y.toInt(),
                    screenWidth * 10,
                    camera.target.y.toInt(),
                    GREEN
                )
            }
        }
        onDraw {
            drawText("SCREEN AREA", 640, 10, 20, RED);

            drawRectangle(0, 0, screenWidth, 5, RED);
            drawRectangle(0, 5, 5, screenHeight - 10, RED);
            drawRectangle(screenWidth - 5, 5, 5, screenHeight - 10, RED);
            drawRectangle(0, screenHeight - 5, screenWidth, 5, RED);

            drawRectangle(10, 10, 250, 113, Fade(SKYBLUE, 0.5f));
            drawRectangleLines(10, 10, 250, 113, BLUE);

            drawText("Free 2D camera controls:", 20, 20, 10, BLACK);
            drawText("- Right/Left to move player", 40, 40, 10, DARKGRAY);
            drawText("- Mouse Wheel to Zoom in-out", 40, 60, 10, DARKGRAY);
            drawText("- A / S to Rotate", 40, 80, 10, DARKGRAY);
            drawText("- R to reset Zoom and Rotation", 40, 100, 10, DARKGRAY);
        }

    }
}
