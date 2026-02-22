package me.raylib.sample.core

import raylib.core.Colors
import raylib.core.window

fun basicScreenManager() {
    window(
        title = "raylib [core] example - basic screen manager",
        initialFps = 2,
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        val currentScreen: GameScreen = GameScreen.LOGO

        gameComponent {
            provideHandlers {}
        }
    }
}

private enum class GameScreen {
    LOGO, TITLE, GAMEPLAY, ENDING
}