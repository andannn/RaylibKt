package me.raylib.sample.core

import me.raylib.sample.isCollisionWith
import raylib.core.Colors
import raylib.core.Colors.DARKGRAY
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.Colors.RAYWHITE
import raylib.core.Gesture
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.window
import raylib.interop.Fade

private const val MAX_GESTURE_STRINGS = 20
fun inputGestures() {
    window(
        title = "raylib [core] example - input gestures",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        registerComponents {
            component("key") {
                var touchPosition = Vector2()
                val touchArea = Rectangle(220f, 10f, screenWidth - 230.0f, screenHeight - 20.0f)
                var currentGesture = Gesture.GESTURE_NONE
                var lastGesture = Gesture.GESTURE_NONE
                val gestureStrings = mutableListOf<String>()
                provideHandlers {
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
        }
    }
}