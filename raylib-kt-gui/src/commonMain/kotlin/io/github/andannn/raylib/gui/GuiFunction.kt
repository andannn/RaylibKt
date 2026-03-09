/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.gui

import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.CValue
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readValue
import kotlinx.cinterop.reinterpret
import io.github.andannn.raylib.base.Font
import io.github.andannn.raylib.base.Rectangle
import raygui.interop.GuiCheckBox
import raygui.interop.GuiDisable
import raygui.interop.GuiEnable
import raygui.interop.GuiGetFont
import raygui.interop.GuiGetState
import raygui.interop.GuiIsLocked
import raygui.interop.GuiListView
import raygui.interop.GuiLock
import raygui.interop.GuiSetAlpha
import raygui.interop.GuiSetFont
import raygui.interop.GuiSetState
import raygui.interop.GuiSliderBar
import raygui.interop.GuiUnlock

interface GuiFunction {
    var guiEnabled: Boolean
    var guiLocked: Boolean
    fun setGuiAlpha(alpha: Float)
    var guiState: GuiState
    var guiFont: CValue<Font>

    fun guiSliderBar(
        bounds: CValue<Rectangle>,
        textLeft: String,
        textRight: String,
        value: CPointer<FloatVar>,
        minValue: Float,
        maxValue: Float
    )

    fun guiCheckBox(bounds: CValue<Rectangle>, text: String, checked: CPointer<BooleanVar>)

    fun guiListView(bounds: CValue<Rectangle>, text: String?, scrollIndex: CPointer<IntVar>, active: CPointer<IntVar>)
}

fun GuiFunction(): GuiFunction = DefaultGuiFunctions()

private class DefaultGuiFunctions : GuiFunction {
    private var _guiEnabled = true
    override var guiEnabled: Boolean
        get() = _guiEnabled
        set(value) {
            if (_guiEnabled != value) {
                if (value) GuiEnable() else GuiDisable()
                _guiEnabled = value
            }
        }
    override var guiLocked: Boolean
        get() = GuiIsLocked()
        set(value) {
            if (GuiIsLocked() != value) {
                if (value) {
                    GuiLock()
                } else {
                    GuiUnlock()
                }
            }
        }

    override fun setGuiAlpha(alpha: Float) {
        GuiSetAlpha(alpha)
    }

    override var guiState: GuiState
        get() = guiStateByValue(GuiGetState()) ?: error("Unknown gui state")
        set(value) {
            GuiSetState(value.value.toInt())
        }

    override var guiFont: CValue<Font>
        get() = GuiGetFont().reinterpret()
        set(value) {
            GuiSetFont(value.reinterpret())
        }

    override fun guiSliderBar(
        bounds: CValue<Rectangle>,
        textLeft: String,
        textRight: String,
        value: CPointer<FloatVar>,
        minValue: Float,
        maxValue: Float
    ) {
        GuiSliderBar(bounds.reinterpret(), textLeft, textRight, value, minValue, maxValue)
    }

    override fun guiCheckBox(
        bounds: CValue<Rectangle>,
        text: String,
        checked: CPointer<BooleanVar>
    ) {
        GuiCheckBox(bounds.reinterpret(), text, checked)
    }

    override fun guiListView(
        bounds: CValue<Rectangle>,
        text: String?,
        scrollIndex: CPointer<IntVar>,
        active: CPointer<IntVar>
    ) {
        GuiListView(bounds.reinterpret(), text, scrollIndex, active)
    }
}

private fun guiStateByValue(value: Int): GuiState? =
    GuiState.entries.find { it.value.toInt() == value }

inline fun <reified S : CStructVar, reified T : CStructVar> CValue<S>.reinterpret(): CValue<T> = memScoped {
    val from = this@reinterpret.getPointer(this)
    val to: CPointer<T> = from.reinterpret()
    to.pointed.readValue()
}
