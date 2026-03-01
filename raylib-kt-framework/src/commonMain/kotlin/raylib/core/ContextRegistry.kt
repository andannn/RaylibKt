package raylib.core

import kotlin.reflect.KClass
import kotlin.reflect.cast


internal class ContextRegistryImpl : ContextRegistry {
    private val map = mutableMapOf<KClass<*>, Any>()

    override fun <T : Any> put(type: KClass<T>, value: T) {
        map[type] = value
    }

    override fun <T : Any> get(type: KClass<T>): T = type.cast(map[type])
}

interface ContextRegistry {
    fun <T : Any> put(type: KClass<T>, value: T)

    fun <T : Any> get(type: KClass<T>): T
}

inline fun <reified T : Any> ContextRegistry.get(): T = get(T::class)
inline fun <reified T : Any> ContextRegistry.put(value: T) = put(T::class, value)
