package me.raylib.sample.core

import raylib.core.Colors.DARKGRAY
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.Colors.RAYWHITE
import raylib.core.ComponentRegistry
import raylib.core.Gesture
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.getValue
import raylib.core.isCollisionWith
import raylib.core.mutableStateOf
import raylib.core.remember
import raylib.core.setValue
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
        onUpdate {
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

        onDraw {
            drawRectangle(touchArea, GRAY)
            drawRectangle(225, 15, screenWidth - 240, screenHeight - 30, RAYWHITE);
            drawText(
                "GESTURES TEST AREA",
                screenWidth - 270,
                screenHeight - 40,
                20,
                Fade(GRAY, 0.5f)
            )
            val gesturesCount = gestureStrings.size
            gestureStrings.forEachIndexed { i, string ->
                if (i % 2 == 0) drawRectangle(10, 30 + 20 * i, 200, 20, Fade(LIGHTGRAY, 0.5f));
                else drawRectangle(10, 30 + 20 * i, 200, 20, Fade(LIGHTGRAY, 0.3f));

                if (i < gesturesCount - 1) {
                    drawText(gestureStrings[i], 35, 36 + 20 * i, 10, DARKGRAY)
                } else {
                    drawText(gestureStrings[i], 35, 36 + 20 * i, 10, MAROON)
                }
            }
        }
    }
}
