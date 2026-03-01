package raylib.core

import kotlin.reflect.KClass
import kotlin.reflect.cast

interface Context

interface ContextProvider {
    fun <T : Context> get(type: KClass<T>): T

}

interface ContextRegistry : ContextProvider {
    fun <T : Context> put(type: KClass<T>, value: T)

}

inline fun <reified T : Context> ContextProvider.get(): T = get(T::class)
inline fun <reified T : Context> ContextRegistry.put(value: T) = put(T::class, value)

internal class ContextRegistryImpl : ContextRegistry {
    private val map = mutableMapOf<KClass<*>, Any>()

    override fun <T : Context> put(type: KClass<T>, value: T) {
        map[type] = value
    }

    override fun <T : Context> get(type: KClass<T>): T = type.cast(map[type])
}
