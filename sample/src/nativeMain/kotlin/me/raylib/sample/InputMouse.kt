package me.raylib.sample

import kotlinx.cinterop.CValue
import raylib.core.Colors
import raylib.core.KeyboardKey
import raylib.core.MouseButton
import raylib.core.Vector2
import raylib.core.drawScope
import raylib.core.mainGameLoop
import raylib.core.window

fun inputMouse() {
    window(
        title = "raylib [core] example - input mouse",
        initialFps = 60,
        width = 800,
        height = 450
    ) {
        var ballPosition: CValue<Vector2>
        mainGameLoop {
            if (KeyboardKey.KEY_H.isPressed()) {
                if (isCursorHidden) {
                    showCursor()
                } else {
                    hideCursor()
                }
            }

            ballPosition = mousePosition
            val ballColor = if (MouseButton.MOUSE_BUTTON_LEFT.isPressed()) {
                Colors.MAROON
            } else if (MouseButton.MOUSE_BUTTON_MIDDLE.isPressed()) {
                Colors.LIME
            } else if (MouseButton.MOUSE_BUTTON_RIGHT.isPressed()) {
                Colors.DARKBLUE
            } else if (MouseButton.MOUSE_BUTTON_SIDE.isPressed()) {
                Colors.PURPLE
            } else if (MouseButton.MOUSE_BUTTON_EXTRA.isPressed()) {
                Colors.YELLOW
            } else if (MouseButton.MOUSE_BUTTON_FORWARD.isPressed()) {
                Colors.ORANGE
            } else if (MouseButton.MOUSE_BUTTON_BACK.isPressed()) {
                Colors.BEIGE
            } else {
                Colors.DARKBLUE
            }

            drawScope(Colors.RAYWHITE) {
                drawCircle(ballPosition, 40f, ballColor)

                drawText(
                    "move ball with mouse and click mouse button to change color",
                    10,
                    10,
                    20,
                    Colors.DARKGRAY
                )
                drawText("Press 'H' to toggle cursor visibility", 10, 30, 20, Colors.DARKGRAY)
                if (isCursorHidden) {
                    drawText("Cursor is hidden", 20, 60, 20, Colors.RED)
                } else {
                    drawText("Cursor is visible", 20, 60, 20, Colors.GREEN)
                }
            }
        }
    }
}