/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core

import kotlin.reflect.KClass
import kotlin.reflect.cast

interface Context

interface ContextProvider

interface ContextRegistry : ContextProvider

inline fun <reified T: Context> ContextProvider.find(): T {
    return findOrNull<T>() ?: error("not found ${T::class}")
}

inline fun <reified T: Context> ContextProvider.findOrNull(): T? {
    this as ContextRegistryInternal
    for (i in keys.lastIndex downTo 0) {
        val clazz = keys[i]
        val instance = values[i]
        if (clazz == T::class) {
            return instance as T
        }
    }
    return null
}

inline fun <reified T : Context> ContextRegistry.provide(instance: T, crossinline block: () -> Unit) {
    this as ContextRegistryInternal
    keys.add(T::class)
    values.add(instance)
    try {
        block()
    } finally {
        keys.removeAt(keys.lastIndex)
        values.removeAt(values.lastIndex)
    }
}

@PublishedApi
internal interface ContextRegistryInternal : ContextRegistry {
    val keys: MutableList<KClass<*>>
    val values: MutableList<Any>
}

internal fun ContextRegistryInternal(): ContextRegistryInternal = ContextRegistryInternalImpl()

private class ContextRegistryInternalImpl : ContextRegistryInternal {
    override val keys = mutableListOf<KClass<*>>()
    override val values = mutableListOf<Any>()
}
