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

fun <T> DisposableRegistry.stateOf(initialValue: NativePlacement.() -> T): DisposableState<T> =
    DisposableState(initialValue).also {
        disposeOnClose(it)
    }

class ManagedStateList<T>(
    private val windowScope: WindowScope,
    private val innerList: MutableList<DisposableState<T>> = mutableListOf()
) : List<DisposableState<T>> by innerList {

    fun addState(state: DisposableState<T>): Disposable = innerList.add(state).let {
        windowScope.invalidComponents()

        state.onDispose = {
            windowScope.invalidComponents()
            removeObjectOnNextFrame(state)
        }
        state
    }

    private fun removeObjectOnNextFrame(state: DisposableState<T>) {
        windowScope.postFrameCallback {
            innerList.remove(state)
            state.clearNative()
        }
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

    internal fun clearNative() {
        isFreed = true
        arena.clear()
    }

    override fun dispose() {
        if (isDisposed) return
        isDisposed = true

        onDispose?.invoke()  // this is not null when managed by state list.
            ?: clearNative() // clear immediately.
    }
}
