package me.raylib.sample.core

import raylib.core.Colors.BLACK
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.component
import raylib.core.getValue
import raylib.core.mutableStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.remember
import raylib.core.setValue
import raylib.interop.WindowShouldClose

fun ComponentRegistry.windowShouldClose(
    exitKey: KeyboardKey = KeyboardKey.KEY_ESCAPE,
) = component("key") {
    setExitKey(exitKey)
    interceptExitKey(true)
    var exitWindowRequested by remember {
        mutableStateOf(false)
    }
    onUpdate {
        if (WindowShouldClose() || exitKey.isPressed()) exitWindowRequested = true
        if (exitWindowRequested) {
            // A request for close window has been issued, we can save data before closing
            // or just show a message asking for confirmation
            if (KeyboardKey.KEY_Y.isPressed()) requestExit()
            else if (KeyboardKey.KEY_N.isPressed()) exitWindowRequested = false
        }
    }

    onDraw {
        if (exitWindowRequested) {
            drawRectangle(0, 100, screenWidth, 200, BLACK)
            drawText("Are you sure you want to exit program? [Y/N]", 40, 180, 30, WHITE)
        }
    }
}
