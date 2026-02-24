package me.raylib.sample.core

import raylib.core.Colors.BLACK
import raylib.core.window

fun monitorDetector() {
    window(
        title = "raylib [core] example - monitor detector",
        width = 800,
        height = 450,
        initialBackGroundColor = BLACK
    ) {
        registerComponents {
            component("key") {
                provideHandlers {

                }
            }
        }
    }
}