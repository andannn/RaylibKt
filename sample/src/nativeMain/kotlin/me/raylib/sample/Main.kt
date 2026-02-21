package me.raylib.sample

import me.raylib.sample.core.inputGestures
import me.raylib.sample.core.inputMultitouch
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
@CName(externName = "raylib_android_main")
fun main() {
//    firstWindow()
//    deltaTime()
//    inputKeys()
//    inputMouse()
//    inputMouseWheel()
//    inputMultitouch()
    inputGestures()
}
