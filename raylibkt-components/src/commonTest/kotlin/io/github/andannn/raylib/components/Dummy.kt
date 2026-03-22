/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.foundation.Color
import io.github.andannn.raylib.foundation.ConfigFlags
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.WindowFunction
import kotlinx.cinterop.CValue

class DummyWindowFunction: WindowFunction {
    override val isDebug: Boolean
        get() = TODO("Not yet implemented")
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
        get() = TODO("Not yet implemented")

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