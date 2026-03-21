/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Texture
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextProvider
import io.github.andannn.raylib.core.DisposableRegistry
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.loadTextureFromImage
import io.github.andannn.raylib.core.provide
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.rres.useImageResource
import io.github.andannn.raylib.rres.useTextResource
import kotlinx.cinterop.CValue
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import raylib.interop.LoadTexture
import raylib.interop.UnloadTexture

class GameAssetsManager(
    private val contextProvider: ContextProvider,
    private val rememberScope: RememberScope
) : Context {
    private val textureMap = mutableMapOf<String, CValue<Texture>>()

    fun getOrCachedTextureFromFile(path: String): CValue<Texture> {
        return textureMap.getOrPut(path) { rememberScope.loadTexture(path) }
    }

    fun getOrCachedTextureFromRres(rres: String, resourceId: UInt): CValue<Texture> {
        return textureMap.getOrPut("${rres}_$resourceId") {
            contextProvider.useImageResource(rres, resourceId) { img ->
                rememberScope.loadTextureFromImage(img)
            }
        }
    }

    fun getTextFromFile(file: String): String {
        return SystemFileSystem.source(Path(file)).use {
            it.buffered().readString()
        }
    }

    fun getTextFromRres(rres: String, resourceId: UInt): String {
        return contextProvider.useTextResource(rres, resourceId)
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

inline fun ComponentRegistry.gameAssetsComponent(
    crossinline block: ComponentScope.() -> Unit
) = component("assetManager") {
    val context: GameAssetsManager = remember {
        GameAssetsManager(this@gameAssetsComponent, this)
    }

    provide(context) {
        block()
    }
}
