package raylib.core

import kotlinx.cinterop.MemScope
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.memScoped


interface WindowContext : NativePlacement {
    val title: String
    val screenWidth: Int
    val screenHeight: Int
    var currentFps: Int

    fun disposeOnClose(disposable: Disposable)
}

fun interface Disposable {
    fun dispose()
}

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    block: WindowContext.() -> Unit
): WindowContext =
    memScoped {
        DefaultWindowContext(
            memoScope = this,
            initialFps = initialFps,
            title = title,
            screenWidth = width,
            screenHeight = height
        )
            .apply(block)
            .also { it.dispose() }
    }

internal class DefaultWindowContext(
    memoScope: MemScope,
    initialFps: Int,
    override val title: String,
    override val screenWidth: Int,
    override val screenHeight: Int,
) : WindowContext, NativePlacement by memoScope {
    private val disposables = mutableListOf<Disposable>()

    init {
        raylib.interop.InitWindow(screenWidth, screenHeight, title)
        raylib.interop.SetTargetFPS(initialFps)
    }

    override var currentFps: Int
        get() = raylib.interop.GetFPS()
        set(value) {
            if (raylib.interop.GetFPS() != value) {
                raylib.interop.SetTargetFPS(value)
            }
        }

    override fun disposeOnClose(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dispose() {
        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}
