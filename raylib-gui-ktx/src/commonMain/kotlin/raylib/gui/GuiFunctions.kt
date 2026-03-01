package raylib.gui

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.CValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readValue
import kotlinx.cinterop.reinterpret
import raylib.core.Font

interface GuiFunctions {
    var guiEnabled: Boolean
    var guiLocked: Boolean
    fun setGuiAlpha(alpha: Float)
    var guiState: GuiState
    var guiFont: CValue<Font>
}

private class DefaultGuiFunctions : GuiFunctions {
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
}

private fun guiStateByValue(value: Int): GuiState? =
    GuiState.entries.find { it.value.toInt() == value }

inline fun <reified S : CStructVar, reified T : CStructVar> CValue<S>.reinterpret(): CValue<T> = memScoped {
    val from = this@reinterpret.getPointer(this)
    val to: CPointer<T> = from.reinterpret()
    to.pointed.readValue()
}

