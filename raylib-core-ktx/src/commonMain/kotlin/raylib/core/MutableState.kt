package raylib.core

import kotlinx.cinterop.Arena
import kotlinx.cinterop.NativePlacement

interface State<out T> {
    val value: T
}

interface MutableState<T> : State<T> {
    override var value: T
}

fun <T> WindowScope.mutableStateOf(initialValue: T): MutableState<T> =
    object : MutableStateBox<T>(initialValue, this) {}

internal abstract class MutableStateBox<T>(
    initialValue: T,
    private val windowScope: WindowScope
) : MutableState<T> {
    private var _field = initialValue
    override var value: T
        get() = _field
        set(value) {
            if (_field == value) return

            _field = value
            (windowScope as? DefaultWindowScope)?.invalidComponents()
        }
}

fun <T> WindowScope.stateListOf(vararg items: DisposableState<T>) =
    ManagedStateList<T>(this)
        .apply {
            items.forEach { addState(it) }
        }

fun <T> WindowScope.disposableState(initialValue: NativePlacement.() -> T): DisposableState<T> =
    object : DisposableState<T>(initialValue, this) {}

class ManagedStateList<T>(
    private val windowScope: WindowScope,
    private val innerList: MutableList<DisposableState<T>> = mutableListOf()
) : List<DisposableState<T>> by innerList {

    fun addState(state: DisposableState<T>) = innerList.add(state).also {
        (windowScope as DefaultWindowScope).invalidComponents()
        state.onRemove = { innerList.remove(state) }
    }
}

abstract class DisposableState<T>(
    initialValue: NativePlacement.() -> T,
    private val windowScope: WindowScope,
) : State<T>, Disposable {
    private val arena: Arena = Arena()
    override val value = arena.initialValue()

    internal var isDisposed = false
    internal var onRemove: (() -> Unit)? = null

    override fun dispose() {
        if (isDisposed) return
        isDisposed = true
        (windowScope as DefaultWindowScope).invalidComponents()

        windowScope.postFrameCallback {
            onRemove?.invoke()
            arena.clear()
        }
    }
}
