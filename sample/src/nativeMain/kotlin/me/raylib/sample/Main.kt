package me.raylib.sample

import raylib.core.Colors
import raylib.core.Colors.LIGHTGRAY
import raylib.core.RlTiming
import raylib.core.RlWindow
import raylib.core.drawScope
import raylib.core.text
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
@CName(externName = "raylib_android_main")
fun main() {
    RlWindow.init(800, 450, "raylib [core] example - basic window")
    RlTiming.setTargetFPS(60)
    while (!RlWindow.shouldClose()) {
        drawScope {
            clearBackground(Colors.RAYWHITE)
            text {
                draw("Congrats! You created your first window!", 190, 200, 20, LIGHTGRAY);
            }
        }
    }
    RlWindow.close()
}