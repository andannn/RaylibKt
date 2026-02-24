package raylib.core

import kotlinx.cinterop.Arena
import kotlinx.cinterop.NativePlacement

interface State<out T> {
    val value: T
}
interface MutableState<T>: State<T> {
    override var value: T
}

fun <T> WindowScope.stateBox(initialValue: T): MutableState<T> =
    object : StateBox<T>(initialValue, this) {}

internal abstract class StateBox<T>(
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

fun <T> WindowScope.stateList(vararg items: DisposableState<T>) =
    ManagedStateList<T>(this)
        .apply{
            items.forEach { addState(it) }
        }
        .also {
            (this as DefaultWindowScope).onPreBuild {
                it.cleanup()
            }
        }

fun <T> WindowScope.disposableState(initialValue: NativePlacement.() -> T): DisposableState<T> =
    object : DisposableState<T>(initialValue, this) {}

class ManagedStateList<T>(
    private val windowScope: WindowScope,
    private val innerList: MutableList<DisposableState<T>> = mutableListOf()
) : List<DisposableState<T>> by innerList {

    fun addState(state: DisposableState<T>) = innerList.add(state).also {
        (windowScope as DefaultWindowScope).invalidComponents()
    }

    internal fun cleanup() {
        innerList.removeAll { state ->
            if (state.isDisposed) {
                state.clearNativeState()
                true
            } else {
                false
            }
        }
    }
}

abstract class DisposableState<T>(
    initialValue: NativePlacement.() -> T,
    private val windowScope: WindowScope,
    private val arena: Arena = Arena(),
) : State<T>, Disposable, NativePlacement by arena {
    override val value = initialValue()

    internal var isDisposed = false

    override fun dispose() {
        if (isDisposed) return
        isDisposed = true
        (windowScope as DefaultWindowScope).invalidComponents()
    }

    internal fun clearNativeState() {
        arena.clear()
    }
}
