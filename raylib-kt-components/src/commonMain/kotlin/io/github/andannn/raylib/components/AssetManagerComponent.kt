package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Texture
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.DisposableRegistry
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.provide
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.CValue
import raylib.interop.LoadTexture
import raylib.interop.UnloadTexture

class AssetManager(
    private val rememberScope: RememberScope
) : Context {
    private val textureMap = mutableMapOf<String, CValue<Texture>>()

    fun getTexture(path: String): CValue<Texture> {
        return textureMap.getOrPut(path) { rememberScope.loadTexture(path) }
    }

// TODO: add fonts

    private fun DisposableRegistry.loadTexture(
        fileName: String,
    ): CValue<Texture> {
        val texture = LoadTexture(fileName)
        disposeOnClose {
            UnloadTexture(texture)
        }
        return texture
    }
}

inline fun ComponentRegistry.assetManagerComponent(
    crossinline block: ComponentScope.() -> Unit
) = component("assetManager") {
    val context: AssetManager = remember {
        AssetManager(this)
    }

    provide(context) {
        block()
    }
}
