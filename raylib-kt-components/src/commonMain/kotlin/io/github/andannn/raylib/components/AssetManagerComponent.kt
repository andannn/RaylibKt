/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.foundation.Texture
import io.github.andannn.raylib.foundation.loadTextureFromImage
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.runtime.Context
import io.github.andannn.raylib.runtime.ContextProvider
import io.github.andannn.raylib.runtime.DisposableRegistry
import io.github.andannn.raylib.runtime.RememberScope
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.find
import io.github.andannn.raylib.runtime.provide
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.rres.ResourceContext
import io.github.andannn.raylib.rres.useImageResource
import io.github.andannn.raylib.rres.useTextResource
import kotlinx.cinterop.CValue
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import raylib.interop.LoadTexture
import raylib.interop.UnloadTexture

fun ContextProvider.rresTextureAsset(rres: String, resourceId: UInt): CValue<Texture> {
    return find<GameAssetsManager>().getOrCachedTextureFromRres(rres, resourceId)
}

fun ContextProvider.rresTextureAsset(path: String): CValue<Texture> {
    return find<GameAssetsManager>().getOrCachedTextureFromRres(path)
}

fun ContextProvider.fileTextureAsset(path: String): CValue<Texture> {
    return find<GameAssetsManager>().getOrCachedTextureFromFile(path)
}

fun ContextProvider.rresTextAsset(rres: String, resourceId: UInt): String {
    return find<GameAssetsManager>().getTextFromRres(rres, resourceId)
}

fun ContextProvider.rresTextAsset(path: String): String {
    return find<GameAssetsManager>().getTextFromRres(path)
}

fun ContextProvider.fileTextAsset(path: String): String {
    return find<GameAssetsManager>().getTextFromFile(path)
}

@PublishedApi
internal class GameAssetsManager(
    private val contextProvider: ContextProvider,
    private val rememberScope: RememberScope,
    private val rresFiles: List<String> = emptyList(),
) : Context {
    private val resourceContext = contextProvider.find<ResourceContext>()
    private val textureMap = mutableMapOf<String, CValue<Texture>>()

    fun getOrCachedTextureFromFile(path: String): CValue<Texture> {
        return textureMap.getOrPut(path) { rememberScope.loadTexture(path) }
    }

    fun getOrCachedTextureFromRres(rres: String, resourceId: UInt): CValue<Texture> {
        return textureMap.getOrPut("$resourceId") {
            contextProvider.useImageResource(rres, resourceId) { img ->
                rememberScope.loadTextureFromImage(img)
            }
        }
    }

    fun getOrCachedTextureFromRres(path: String): CValue<Texture> {
        val (id, rresFile) = resolveResourceId(path)
        return textureMap.getOrPut("${rresFile}_$id") {
            contextProvider.useImageResource(rresFile, id) { img ->
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

    fun getTextFromRres(path: String): String {
        val (id, rresFile) = resolveResourceId(path)
        return getTextFromRres(rresFile, id)
    }

    private fun resolveResourceId(path: String): Pair<UInt, String> {
        var rresFile: String? = null
        var id: UInt? = null
        for (rres in rresFiles) {
            val centralDir = resourceContext.loadCentralDirectory(rres)
            val resourceId = resourceContext.getResourceId(centralDir, path)
            resourceContext.unloadCentralDirectory(centralDir)
            if (resourceId == 0u) continue
            id = resourceId
            rresFile = rres
        }
        return (id ?: error("id not found at path $path")) to rresFile!!
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
    rresFiles: List<String> = emptyList(),
    crossinline block: ComponentScope.() -> Unit
) = component("assetManager") {
    val context: GameAssetsManager = remember {
        GameAssetsManager(this@gameAssetsComponent, this, rresFiles)
    }

    provide(context) {
        block()
    }
}
