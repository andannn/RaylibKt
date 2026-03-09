package me.raylib.sample.core

import kotlinx.cinterop.CValue
import io.github.andannn.raylib.base.Colors
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.inputMouse() {
    component("key") {
        var ballPosition: CValue<Vector2> by remember {
            mutableStateOf(Vector2())
        }
        var ballColor by remember {
            mutableStateOf(Colors.DARKBLUE)
        }
        var cursorHidden by remember {
            mutableStateOf(false)
        }

        onUpdate {
            cursorHidden = isCursorHidden
            if (KeyboardKey.KEY_H.isPressed()) {
                if (isCursorHidden) {
                    showCursor()
                } else {
                    hideCursor()
                }
            }

            ballPosition = mousePosition
            ballColor = if (MouseButton.MOUSE_BUTTON_LEFT.isPressed()) {
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
        }
        onDraw {
            drawCircle(ballPosition, 40f, ballColor)
            drawText(
                "move ball with mouse and click mouse button to change color",
                10,
                10,
                20,
                Colors.DARKGRAY
            )
            drawText("Press 'H' to toggle cursor visibility", 10, 30, 20, Colors.DARKGRAY)
            if (cursorHidden) {
                drawText("Cursor is hidden", 20, 60, 20, Colors.RED)
            } else {
                drawText("Cursor is visible", 20, 60, 20, Colors.GREEN)
            }
        }
    }
}