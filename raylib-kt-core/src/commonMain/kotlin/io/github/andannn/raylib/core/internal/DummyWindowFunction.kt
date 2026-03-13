/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core.internal

import io.github.andannn.raylib.base.AudioFunction
import kotlinx.cinterop.CValue
import io.github.andannn.raylib.base.Color
import io.github.andannn.raylib.base.ConfigFlags
import io.github.andannn.raylib.base.FontFunction
import io.github.andannn.raylib.base.ImageFunction
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.core.WindowContext
import io.github.andannn.raylib.core.WindowContextImpl
import io.github.andannn.raylib.base.WindowFunction

fun DummyWindowContextImpl(): WindowContext {
    return WindowContextImpl(DummyWindowFunction(true), ImageFunction() , FontFunction(), AudioFunction())
}
class DummyWindowFunction(override val isDebug: Boolean) : WindowFunction {
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