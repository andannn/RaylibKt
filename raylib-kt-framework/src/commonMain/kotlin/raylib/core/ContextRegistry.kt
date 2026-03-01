package raylib.core

import kotlin.reflect.KClass
import kotlin.reflect.cast

interface Context

internal class ContextRegistryImpl : ContextRegistry {
    private val map = mutableMapOf<KClass<*>, Any>()

    override fun <T : Context> put(type: KClass<T>, value: T) {
        map[type] = value
    }

    override fun <T : Context> get(type: KClass<T>): T = type.cast(map[type])
}

interface ContextRegistry {
    fun <T : Context> put(type: KClass<T>, value: T)

    fun <T : Context> get(type: KClass<T>): T
}

inline fun <reified T : Context> ContextRegistry.get(): T = get(T::class)
inline fun <reified T : Context> ContextRegistry.put(value: T) = put(T::class, value)
