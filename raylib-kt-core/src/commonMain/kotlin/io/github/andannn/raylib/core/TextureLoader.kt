/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.Image
import io.github.andannn.raylib.base.RenderTexture
import io.github.andannn.raylib.base.Texture
import io.github.andannn.raylib.base.Texture2D
import kotlinx.cinterop.CValue
import raylib.interop.LoadRenderTexture
import raylib.interop.LoadTexture
import raylib.interop.LoadTextureFromImage
import raylib.interop.UnloadRenderTexture
import raylib.interop.UnloadTexture

fun DisposableRegistry.loadRenderTexture(
    width: Int,
    height: Int,
): CValue<RenderTexture> {
    val texture = LoadRenderTexture(width, height)
    disposeOnClose {
        UnloadRenderTexture(texture)
    }
    return texture
}

fun DisposableRegistry.loadTexture(
    fileName: String,
): CValue<Texture> {
    val texture = LoadTexture(fileName)
    disposeOnClose {
        UnloadTexture(texture)
    }
    return texture
}

fun DisposableRegistry.loadTextureFromImage(
    image: CValue<Image>
): CValue<Texture2D> {
    val texture = LoadTextureFromImage(image)
    disposeOnClose {
        UnloadTexture(texture)
    }
    return texture
}