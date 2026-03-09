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

fun <T> RememberScope.mutableStateListOf() = ManagedStateList<T>(this)

fun <T> RememberScope.nativeStateOf(initialValue: NativePlacement.() -> T): NativeState<T> =
    NativeState(initialValue).also {
        disposeOnClose(it)
    }

class ManagedStateList<T>(
    private val scope: RememberScope,
    private val innerList: MutableList<NativeState<T>> = mutableListOf()
) : List<NativeState<T>> by innerList {

    fun addState(init: RememberScope.() -> NativeState<T>): Disposable {
        val state = scope.init()

        innerList.add(state)

        state.onDispose = {
            innerList.remove(state)
        }

        return state
    }
}

class NativeState<T>(
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
