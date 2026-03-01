package raylib.core.internal

import kotlinx.cinterop.CValue
import raylib.core.Color
import raylib.core.ConfigFlags
import raylib.core.KeyboardKey
import raylib.core.WindowFunction

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
}