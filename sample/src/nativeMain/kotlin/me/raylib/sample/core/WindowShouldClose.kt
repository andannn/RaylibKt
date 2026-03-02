package me.raylib.sample.core

import raylib.core.Colors.BLACK
import raylib.core.Colors.WHITE
import raylib.core.ComponentFactory
import raylib.core.KeyboardKey
import raylib.core.WindowContext
import raylib.core.window
import raylib.interop.WindowShouldClose

fun ComponentFactory.windowShouldClose(
    exitKey: KeyboardKey = KeyboardKey.KEY_ESCAPE,
) = component("key") {
    setExitKey(exitKey)
    interceptExitKey(true)
    var exitWindowRequested = false
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
