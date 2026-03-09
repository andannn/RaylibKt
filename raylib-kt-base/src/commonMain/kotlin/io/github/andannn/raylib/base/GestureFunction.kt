/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import raylib.interop.GetGestureDetected
import raylib.interop.GetTouchPointCount
import raylib.interop.GetTouchPosition
import raylib.interop.IsGestureDetected

interface GestureFunction {
    val touchPointCount: Int
    fun touchPosition(index: Int): CValue<Vector2>

    fun touchPositions(): Sequence<CValue<Vector2>>

    fun Gesture.isDetected(): Boolean
    val gestureDetected: Gesture
}

fun GestureFunction(): GestureFunction {
    return DefaultTouchFunction()
}

private class DefaultTouchFunction : GestureFunction {
    override val touchPointCount: Int
        get() = GetTouchPointCount()

    override fun touchPosition(index: Int): CValue<Vector2> {
        return GetTouchPosition(index)
    }

    override fun touchPositions(): Sequence<CValue<Vector2>> = sequence {
        repeat(touchPointCount) { index ->
            yield(touchPosition(index))
        }
    }

    override fun Gesture.isDetected(): Boolean {
        return IsGestureDetected(value)
    }

    override val gestureDetected: Gesture
        get() = gestureByValue(GetGestureDetected()) ?: error("Unknown gesture")
}

private fun gestureByValue(value: Int): Gesture? =
    Gesture.entries.find { it.value.toInt() == value }
