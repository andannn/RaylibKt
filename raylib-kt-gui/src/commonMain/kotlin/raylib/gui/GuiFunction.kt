/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.gui

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
import raylib.core.Font
import raylib.core.Rectangle

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
                if (value) raygui.interop.GuiEnable() else raygui.interop.GuiDisable()
                _guiEnabled = value
            }
        }
    override var guiLocked: Boolean
        get() = raygui.interop.GuiIsLocked()
        set(value) {
            if (raygui.interop.GuiIsLocked() != value) {
                if (value) {
                    raygui.interop.GuiLock()
                } else {
                    raygui.interop.GuiUnlock()
                }
            }
        }

    override fun setGuiAlpha(alpha: Float) {
        raygui.interop.GuiSetAlpha(alpha)
    }

    override var guiState: GuiState
        get() = guiStateByValue(raygui.interop.GuiGetState()) ?: error("Unknown gui state")
        set(value) {
            raygui.interop.GuiSetState(value.value.toInt())
        }

    override var guiFont: CValue<Font>
        get() = raygui.interop.GuiGetFont().reinterpret()
        set(value) {
            raygui.interop.GuiSetFont(value.reinterpret())
        }

    override fun guiSliderBar(
        bounds: CValue<Rectangle>,
        textLeft: String,
        textRight: String,
        value: CPointer<FloatVar>,
        minValue: Float,
        maxValue: Float
    ) {
        raygui.interop.GuiSliderBar(bounds.reinterpret(), textLeft, textRight, value, minValue, maxValue)
    }

    override fun guiCheckBox(
        bounds: CValue<Rectangle>,
        text: String,
        checked: CPointer<BooleanVar>
    ) {
        raygui.interop.GuiCheckBox(bounds.reinterpret(), text, checked)
    }

    override fun guiListView(
        bounds: CValue<Rectangle>,
        text: String?,
        scrollIndex: CPointer<IntVar>,
        active: CPointer<IntVar>
    ) {
        raygui.interop.GuiListView(bounds.reinterpret(), text, scrollIndex, active)
    }
}

private fun guiStateByValue(value: Int): GuiState? =
    GuiState.entries.find { it.value.toInt() == value }

inline fun <reified S : CStructVar, reified T : CStructVar> CValue<S>.reinterpret(): CValue<T> = memScoped {
    val from = this@reinterpret.getPointer(this)
    val to: CPointer<T> = from.reinterpret()
    to.pointed.readValue()
}
