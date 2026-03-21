package me.raylib.sample.shape

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.GREEN
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.MouseButton
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.foundation.isCollisionWith
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.runtime.setValue
import raylib.interop.Fade

private const val MOUSE_SCALE_MARK_SIZE = 12

fun ComponentRegistry.rectangleScaling() {
    component("key") {
        val rec by remember {
            val nativeStateOf = nativeStateOf { alloc<Rectangle> { x = 100f; y = 100f; width = 200f; height = 80f } }
            nativeStateOf
        }

        val mousePosition by remember {
            nativeStateOf { alloc<Vector2>() }
        }

        var mouseScaleReady by remember {
            mutableStateOf(false)
        }
        var mouseScaleMode by remember {
            mutableStateOf(false)
        }

        update {
            mousePosition.x = mouseX.toFloat()
            mousePosition.y = mouseY.toFloat()

            if (mousePosition.readValue().isCollisionWith(
                    Rectangle(
                        rec.x + rec.width - MOUSE_SCALE_MARK_SIZE,
                        rec.y + rec.height - MOUSE_SCALE_MARK_SIZE,
                        MOUSE_SCALE_MARK_SIZE.toFloat(),
                        MOUSE_SCALE_MARK_SIZE.toFloat()
                    )
                )
            ) {
                mouseScaleReady = true
                if (MouseButton.MOUSE_BUTTON_LEFT.isPressed()) mouseScaleMode = true
            } else {
                mouseScaleReady = false
            }

            if (mouseScaleMode) {
                mouseScaleReady = true;

                rec.width = (mousePosition.x - rec.x);
                rec.height = (mousePosition.y - rec.y);

                if (rec.width < MOUSE_SCALE_MARK_SIZE) rec.width = MOUSE_SCALE_MARK_SIZE.toFloat()
                if (rec.height < MOUSE_SCALE_MARK_SIZE) rec.height = MOUSE_SCALE_MARK_SIZE.toFloat()

                if (rec.width > (screenWidth - rec.x)) rec.width = screenWidth - rec.x
                if (rec.height > (screenHeight - rec.y)) rec.height = screenHeight - rec.y

                if (MouseButton.MOUSE_BUTTON_LEFT.isReleased()) mouseScaleMode = false
            }
        }
        draw {
            drawText("Scale rectangle dragging from bottom-right corner!", 10, 10, 20, GRAY)
            drawRectangle(rec.readValue(), Fade(GREEN, 0.5f))
            if (mouseScaleReady) {
                drawRectangleLines(rec.readValue(), 1f, RED)
                drawTriangle(
                    Vector2(rec.x + rec.width - MOUSE_SCALE_MARK_SIZE, rec.y + rec.height),
                    Vector2(rec.x + rec.width, rec.y + rec.height),
                    Vector2(rec.x + rec.width, rec.y + rec.height - MOUSE_SCALE_MARK_SIZE), RED
                )
            }

        }
    }
}
