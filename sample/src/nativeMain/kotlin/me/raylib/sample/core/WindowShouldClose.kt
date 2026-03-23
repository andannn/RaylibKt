package me.raylib.sample.core

import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.runtime.setValue
import io.github.andannn.raylib.foundation.windowContext
import raylib.interop.WindowShouldClose

fun ComponentRegistry.windowShouldClose(
    exitKey: KeyboardKey = KeyboardKey.KEY_ESCAPE,
) = component("key") {
    windowContext.setExitKey(exitKey)
    windowContext.interceptExitKey(true)
    var exitWindowRequested by remember {
        mutableStateOf(false)
    }
    update {
        if (WindowShouldClose() || exitKey.isPressed()) exitWindowRequested = true
        if (exitWindowRequested) {
            // A request for close window has been issued, we can save data before closing
            // or just show a message asking for confirmation
            if (KeyboardKey.KEY_Y.isPressed()) requestExit()
            else if (KeyboardKey.KEY_N.isPressed()) exitWindowRequested = false
        }
    }

    draw {
        if (exitWindowRequested) {
            drawRectangle(0, 100, screenWidth, 200, BLACK)
            drawText("Are you sure you want to exit program? [Y/N]", 40, 180, 30f, WHITE)
        }
    }
}
