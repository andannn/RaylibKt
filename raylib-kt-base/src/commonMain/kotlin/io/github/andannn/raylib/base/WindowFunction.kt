/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import raylib.interop.ClearWindowState
import raylib.interop.GetFPS
import raylib.interop.GetFrameTime
import raylib.interop.InitWindow
import raylib.interop.IsWindowState
import raylib.interop.MaximizeWindow
import raylib.interop.MinimizeWindow
import raylib.interop.RestoreWindow
import raylib.interop.SetExitKey
import raylib.interop.SetTargetFPS
import raylib.interop.SetWindowState
import raylib.interop.ToggleBorderlessWindowed
import raylib.interop.ToggleFullscreen
import raylib.interop.WindowShouldClose

interface WindowFunction {
    val isDebug: Boolean
    var backGroundColor: CValue<Color>?
    val title: String
    val screenWidth: Int
    val screenHeight: Int
    var currentFps: Int
    val frameTimeSeconds: Float
    fun ConfigFlags.isEnabled(): Boolean
    fun ConfigFlags.clear()
    fun ConfigFlags.set()
    fun setExitKey(key: KeyboardKey)
    fun interceptExitKey(intercept: Boolean)
    fun requestExit()
    fun toggleFullScreen()
    fun toggleBorderlessWindowed()
    fun minimizeWindow()
    fun maximizeWindow()
    fun restoreWindow()

    fun shouldExit(): Boolean
}

fun WindowFunction(
    initialFps: Int,
    title: String,
    screenWidth: Int,
    screenHeight: Int,
    backGroundColor: CValue<Color>? = null,
    isDebug: Boolean = false,
): WindowFunction = DefaultWindowFunction(
    initialFps,
    title,
    screenWidth,
    screenHeight,
    backGroundColor,
    isDebug,
)

internal class DefaultWindowFunction(
    initialFps: Int,
    override val title: String,
    override val screenWidth: Int,
    override val screenHeight: Int,
    override var backGroundColor: CValue<Color>? = null,
    override val isDebug: Boolean
) : WindowFunction {

    override val frameTimeSeconds: Float
        get() = GetFrameTime()

    init {
        InitWindow(screenWidth, screenHeight, title)
        SetTargetFPS(initialFps)
    }

    internal var interceptExitKey = false
    internal var exitWindowRequest = false

    override fun interceptExitKey(intercept: Boolean) {
        interceptExitKey = intercept
    }

    override fun requestExit() {
        exitWindowRequest = true
    }

    override fun ConfigFlags.isEnabled(): Boolean {
        return IsWindowState(value)
    }

    override fun ConfigFlags.clear() {
        ClearWindowState(value)
    }

    override fun ConfigFlags.set() {
        SetWindowState(value)
    }

    override fun setExitKey(key: KeyboardKey) {
        SetExitKey(key.value.toInt())
    }


    override var currentFps: Int
        get() = GetFPS()
        set(value) {
            if (GetFPS() != value) {
                SetTargetFPS(value)
            }
        }

    override fun toggleFullScreen() {
        ToggleFullscreen()
    }

    override fun toggleBorderlessWindowed() {
        ToggleBorderlessWindowed()
    }

    override fun minimizeWindow() {
        MinimizeWindow()
    }

    override fun maximizeWindow() {
        MaximizeWindow()
    }

    override fun restoreWindow() {
        RestoreWindow()
    }

    override fun shouldExit(): Boolean =
        if (interceptExitKey) {
            exitWindowRequest
        } else {
            WindowShouldClose()
        }
}

