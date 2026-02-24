package me.raylib.sample.core

import raylib.core.Colors
import raylib.core.Colors.BLUE
import raylib.core.Colors.DARKBLUE
import raylib.core.Colors.DARKGREEN
import raylib.core.Colors.GRAY
import raylib.core.Colors.GREEN
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.Colors.PURPLE
import raylib.core.GameComponentsRegisterScope
import raylib.core.Gesture
import raylib.core.KeyboardKey
import raylib.core.window

fun basicScreenManager() {
    window(
        title = "raylib [core] example - basic screen manager",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        var currentScreen = GameScreen.LOGO
        registerGameComponents {
            component("updater") {
                var framesCounter = 0
                provideHandlers {
                    onUpdate {
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
            }
            if (currentScreen == GameScreen.LOGO) screen(GameScreen.LOGO)
            if (currentScreen == GameScreen.TITLE) screen(GameScreen.TITLE)
            if (currentScreen == GameScreen.GAMEPLAY) screen(GameScreen.GAMEPLAY)
            if (currentScreen == GameScreen.ENDING) screen(GameScreen.ENDING)
        }
    }
}

private fun GameComponentsRegisterScope.screen(screen: GameScreen) {
    component(screen) {
        provideHandlers {
            onDraw {
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
}

private enum class GameScreen {
    LOGO, TITLE, GAMEPLAY, ENDING
}