package me.raylib.sample.core

import kotlinx.cinterop.CValue
import io.github.andannn.raylib.base.Color
import io.github.andannn.raylib.base.Colors
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.remember

internal fun ComponentRegistry.firstWindow(
    initialBackGroundColor: CValue<Color> = Colors.RAYWHITE,
) {
    component("key") {
        remember {
            backGroundColor = initialBackGroundColor
        }
        draw {
            drawText("Congrats! You created your first window!", 190, 200, 20, LIGHTGRAY);
        }
    }
}