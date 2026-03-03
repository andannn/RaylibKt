package raylib.core

import kotlinx.cinterop.Arena
import kotlinx.cinterop.NativePlacement
import kotlin.reflect.KProperty

interface State<out T> {
    val value: T
}

interface MutableState<T> : State<T> {
    override var value: T
}

fun <T> mutableStateOf(initialValue: T): MutableState<T> =
    object : MutableStateBox<T>(initialValue) {}

internal abstract class MutableStateBox<T>(
    initialValue: T,
) : MutableState<T> {
    private var _field = initialValue
    override var value: T
        get() = _field
        set(value) {
            if (_field == value) return
            _field = value
        }
}

fun <T> mutableStateListOf(vararg items: DisposableState<T>) =
    ManagedStateList<T>()
        .apply {
            items.forEach { addState(it) }
        }

fun <T> DisposableRegistry.nativeStateOf(initialValue: NativePlacement.() -> T): DisposableState<T> =
    DisposableState(initialValue).also {
        disposeOnClose(it)
    }

class ManagedStateList<T>(
    private val innerList: MutableList<DisposableState<T>> = mutableListOf()
) : List<DisposableState<T>> by innerList {

    fun addState(state: DisposableState<T>): Disposable = innerList.add(state).let {
        state.onDispose = {
            innerList.remove(state)
        }
        state
    }
}

class DisposableState<T>(
    initialValue: NativePlacement.() -> T,
) : State<T>, Disposable {
    private val arena: Arena = Arena()
    override val value = arena.initialValue()

    internal var isDisposed = false
    internal var isFreed = false
    internal var onDispose: (() -> Unit)? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun dispose() {
        if (isDisposed) return
        isDisposed = true

        onDispose?.invoke()
        clearNative()
    }


    private fun clearNative() {
        isFreed = true
        arena.clear()
    }
}
