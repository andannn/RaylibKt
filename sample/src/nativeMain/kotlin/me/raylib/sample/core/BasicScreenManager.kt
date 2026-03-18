package me.raylib.sample.core

import io.github.andannn.raylib.base.Colors.BLUE
import io.github.andannn.raylib.base.Colors.DARKBLUE
import io.github.andannn.raylib.base.Colors.DARKGREEN
import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.GREEN
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.Colors.MAROON
import io.github.andannn.raylib.base.Colors.PURPLE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.Gesture
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.basicScreenManager() {
    var currentScreen by remember {
        mutableStateOf(GameScreen.LOGO)
    }

    component("updater") {
        var framesCounter by remember {
            mutableStateOf(0)
        }

        update {
            when (currentScreen) {
                GameScreen.LOGO -> {
                    framesCounter++
                    if (framesCounter > 120) {
                        currentScreen = GameScreen.TITLE
                    }
                }

                GameScreen.TITLE -> {
                    if (KeyboardKey.KEY_ENTER.isPressed() || Gesture.GESTURE_TAP.isDetected()) {
                        currentScreen = GameScreen.GAMEPLAY
                    }
                }

                GameScreen.GAMEPLAY -> {
                    if (KeyboardKey.KEY_ENTER.isPressed() || Gesture.GESTURE_TAP.isDetected()) {
                        currentScreen = GameScreen.ENDING
                    }
                }

                GameScreen.ENDING -> {
                    if (KeyboardKey.KEY_ENTER.isPressed() || Gesture.GESTURE_TAP.isDetected()) {
                        currentScreen = GameScreen.TITLE
                    }
                }
            }
        }

    }
    if (currentScreen == GameScreen.LOGO) screen(GameScreen.LOGO)
    if (currentScreen == GameScreen.TITLE) screen(GameScreen.TITLE)
    if (currentScreen == GameScreen.GAMEPLAY) screen(GameScreen.GAMEPLAY)
    if (currentScreen == GameScreen.ENDING) screen(GameScreen.ENDING)
}

private fun ComponentRegistry.screen(screen: GameScreen) {
    component(screen) {
        draw {
            when (screen) {
                GameScreen.LOGO -> {
                    // TODO: Draw LOGO screen here!
                    drawText("LOGO SCREEN", 20, 20, 40, LIGHTGRAY)
                    drawText("WAIT for 2 SECONDS...", 290, 220, 20, GRAY)
                }

                GameScreen.TITLE -> {
                    // TODO: draw TITLE screen here!
                    drawRectangle(0, 0, screenWidth, screenHeight, GREEN)
                    drawText("TITLE SCREEN", 20, 20, 40, DARKGREEN)
                    drawText("PRESS ENTER or TAP to JUMP to GAMEPLAY SCREEN", 120, 220, 20, DARKGREEN)
                }

                GameScreen.GAMEPLAY -> {
                    // TODO: draw GAMEPLAY screen here!
                    drawRectangle(0, 0, screenWidth, screenHeight, PURPLE)
                    drawText("GAMEPLAY SCREEN", 20, 20, 40, MAROON)
                    drawText("PRESS ENTER or TAP to JUMP to ENDING SCREEN", 130, 220, 20, MAROON)
                }

                GameScreen.ENDING -> {
                    // TODO: draw ENDING screen here!
                    drawRectangle(0, 0, screenWidth, screenHeight, BLUE)
                    drawText("ENDING SCREEN", 20, 20, 40, DARKBLUE)
                    drawText("PRESS ENTER or TAP to RETURN to TITLE SCREEN", 120, 220, 20, DARKBLUE)
                }
            }
        }
    }
}

private enum class GameScreen {
    LOGO, TITLE, GAMEPLAY, ENDING
}