package me.raylib.sample.core

import io.github.andannn.raylib.foundation.Colors.DARKGRAY
import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.foundation.Colors.RAYWHITE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Gesture
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.foundation.isCollisionWith
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.setValue
import raylib.interop.Fade

private const val MAX_GESTURE_STRINGS = 20
fun ComponentRegistry.inputGestures() {
    component("key") {
        val touchArea = remember {
            Rectangle(220f, 10f, screenWidth - 230.0f, screenHeight - 20.0f)
        }
        var touchPosition by remember {
            mutableStateOf(Vector2())
        }
        var currentGesture by remember {
            mutableStateOf(Gesture.GESTURE_NONE)
        }
        var lastGesture by remember {
            mutableStateOf(Gesture.GESTURE_NONE)
        }
        val gestureStrings = remember {
            mutableListOf<String>()
        }
        update {
            lastGesture = currentGesture
            currentGesture = gestureDetected
            touchPosition = touchPosition(0)
            if (touchPosition.isCollisionWith(touchArea) && currentGesture != Gesture.GESTURE_NONE) {
                if (currentGesture != lastGesture) {
                    gestureStrings.add(currentGesture.name)
                }

                if (gestureStrings.size > MAX_GESTURE_STRINGS) {
                    gestureStrings.clear()
                }
            }
        }

        draw {
            drawRectangle(touchArea, GRAY)
            drawRectangle(225, 15, screenWidth - 240, screenHeight - 30, RAYWHITE);
            drawText(
                "GESTURES TEST AREA",
                screenWidth - 270,
                screenHeight - 40,
                20f,
                Fade(GRAY, 0.5f)
            )
            val gesturesCount = gestureStrings.size
            gestureStrings.forEachIndexed { i, string ->
                if (i % 2 == 0) drawRectangle(10, 30 + 20 * i, 200, 20, Fade(LIGHTGRAY, 0.5f));
                else drawRectangle(10, 30 + 20 * i, 200, 20, Fade(LIGHTGRAY, 0.3f));

                if (i < gesturesCount - 1) {
                    drawText(gestureStrings[i], 35, 36 + 20 * i, 10f, DARKGRAY)
                } else {
                    drawText(gestureStrings[i], 35, 36 + 20 * i, 10f, MAROON)
                }
            }
        }
    }
}
