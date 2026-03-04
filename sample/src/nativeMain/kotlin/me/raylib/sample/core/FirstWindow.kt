package me.raylib.sample.core

import kotlinx.cinterop.CValue
import raylib.core.Color
import raylib.core.Colors
import raylib.core.Colors.LIGHTGRAY
import raylib.core.ComponentRegistry

internal fun ComponentRegistry.firstWindow(
    initialBackGroundColor: CValue<Color> = Colors.RAYWHITE,
) {
    component("key") {
        backGroundColor = initialBackGroundColor
        onDraw {
            drawText("Congrats! You created your first window!", 190, 200, 20, LIGHTGRAY);
        }
    }
}