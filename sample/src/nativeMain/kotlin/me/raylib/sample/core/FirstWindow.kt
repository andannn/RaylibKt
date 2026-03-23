package me.raylib.sample.core

import kotlinx.cinterop.CValue
import io.github.andannn.raylib.foundation.Color
import io.github.andannn.raylib.foundation.Colors
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.windowContext

internal fun ComponentRegistry.firstWindow(
    initialBackGroundColor: CValue<Color> = Colors.RAYWHITE,
) {
    component("key") {
        remember {
            windowContext.backGroundColor = initialBackGroundColor
        }
        draw {
            drawText("Congrats! You created your first window!", 190, 200, 20f, LIGHTGRAY);
        }
    }
}