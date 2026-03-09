/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core.internal

import kotlinx.cinterop.CValue
import raylib.core.Color
import raylib.core.ConfigFlags
import raylib.core.FontFunction
import raylib.core.ImageFunction
import raylib.core.KeyboardKey
import raylib.core.WindowContext
import raylib.core.WindowContextImpl
import raylib.core.WindowFunction

fun DummyWindowContextImpl(): WindowContext {
    return WindowContextImpl(DummyWindowFunction(), ImageFunction() , FontFunction())
}
class DummyWindowFunction: WindowFunction {
    override var backGroundColor: CValue<Color>?
        get() = TODO("Not yet implemented")
        set(value) {}
    override val title: String
        get() = TODO("Not yet implemented")
    override val screenWidth: Int
        get() = TODO("Not yet implemented")
    override val screenHeight: Int
        get() = TODO("Not yet implemented")
    override var currentFps: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    override val frameTimeSeconds: Float
        get() = 1f

    override fun ConfigFlags.isEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun ConfigFlags.clear() {
        TODO("Not yet implemented")
    }

    override fun ConfigFlags.set() {
        TODO("Not yet implemented")
    }

    override fun setExitKey(key: KeyboardKey) {
        TODO("Not yet implemented")
    }

    override fun interceptExitKey(intercept: Boolean) {
        TODO("Not yet implemented")
    }

    override fun requestExit() {
        TODO("Not yet implemented")
    }

    override fun toggleFullScreen() {
        TODO("Not yet implemented")
    }

    override fun toggleBorderlessWindowed() {
        TODO("Not yet implemented")
    }

    override fun minimizeWindow() {
        TODO("Not yet implemented")
    }

    override fun maximizeWindow() {
        TODO("Not yet implemented")
    }

    override fun restoreWindow() {
        TODO("Not yet implemented")
    }

    override fun shouldExit(): Boolean {
        TODO("Not yet implemented")
    }
}