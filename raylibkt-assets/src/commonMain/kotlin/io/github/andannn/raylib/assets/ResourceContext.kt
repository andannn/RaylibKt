/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.assets

import io.github.andannn.raylib.foundation.Image
import io.github.andannn.raylib.runtime.Context
import io.github.andannn.raylib.runtime.ContextProvider
import io.github.andannn.raylib.runtime.find
import kotlinx.cinterop.CValue
import kotlinx.cinterop.toKString

interface ResourceContext : Context, RresFunction, RaylibRresFunction

fun ResourceContext(
    rresFunction: RresFunction = RresFunction(),
    raylibRresFunction: RaylibRresFunction = RaylibRresFunction(),
): ResourceContext = object : ResourceContext,
    RresFunction by rresFunction,
    RaylibRresFunction by raylibRresFunction {}

inline fun <reified T> ContextProvider.useResource(
    rresFile: String,
    resourceId: UInt,
    block: ResourceContext.(CValue<ResourceChunk>) -> T
) = with(find<ResourceContext>()) {
    val chunk = loadResourceChunk(rresFile, resourceId)
    val ret = block(chunk)
    unloadResourceChunk(chunk)
    ret
}

inline fun <reified T> ContextProvider.useImageResource(
    rresFile: String,
    resourceId: UInt,
    block: (CValue<Image>) -> T
) = useResource(rresFile, resourceId) { chunk ->
    block(loadImageFromResource(chunk))
}

inline fun ContextProvider.useTextResource(
    rresFile: String,
    resourceId: UInt,
    block: (String) -> String = { it }
) = useResource(rresFile, resourceId) { chunk ->
    block(loadTextFromResource(chunk)?.toKString() ?: error("No string found"))
}
