/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import kotlinx.cinterop.Arena
import kotlinx.cinterop.NativePlacement
import kotlin.reflect.KProperty

interface State<out T> {
    val value: T
}

interface MutableState<T> : State<T> {
    override var value: T
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> MutableState<T>.setValue(
    thisObj: Any?,
    property: KProperty<*>,
    value: T,
) {
    this.value = value
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> State<T>.getValue(thisObj: Any?, property: KProperty<*>): T = value

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

fun <T> RememberScope.mutableStateListOf() = ManagedStateList<T>()
    .also {
        disposeOnClose(it)
    }

fun <T> RememberScope.nativeStateOf(initialValue: NativePlacement.() -> T): NativeState<T> =
    NativeState(initialValue).also {
        disposeOnClose(it)
    }

inline fun <reified T> ComponentRegistry.components(
    items: ManagedStateList<T>,
    crossinline key: (T) -> Any,
    crossinline block: ComponentScope.(NativeState<T>) -> Unit
) {
    items.downEach { _, element ->
        val key = key(element.value)

        component(key) {
            block.invoke(this, element)
        }
    }
}

@PublishedApi
internal inline fun <T> ManagedStateList<T>.downEach(block: (index: Int, element: NativeState<T>) -> Unit) {
    for (i in size - 1 downTo 0) {
        if (i < size) {
            block(i, this[i])
        }
    }
}

class ManagedStateList<T>(
    private val innerList: MutableList<NativeState<T>> = mutableListOf()
) : List<NativeState<T>> by innerList, Disposable {

    fun addState(init: NativePlacement.() -> T): NativeState<T> {
        val state = NativeState(initialValue = init)

        innerList.add(state)

        state.onDispose = {
            innerList.remove(state)
        }

        return state
    }

    override fun dispose() {
        for (i in innerList.lastIndex downTo 0) {
            innerList[i].dispose()
        }
    }
}

class NativeState<T>(
    initialValue: NativePlacement.() -> T,
    private val arena: Arena = Arena()
) : State<T>, Disposable {
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
