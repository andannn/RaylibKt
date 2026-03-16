package me.raylib.sample.core

import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.WHITE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue
import raylib.interop.WindowShouldClose

fun ComponentRegistry.windowShouldClose(
    exitKey: KeyboardKey = KeyboardKey.KEY_ESCAPE,
) = component("key") {
    setExitKey(exitKey)
    interceptExitKey(true)
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
            drawText("Are you sure you want to exit program? [Y/N]", 40, 180, 30, WHITE)
        }
    }
}
