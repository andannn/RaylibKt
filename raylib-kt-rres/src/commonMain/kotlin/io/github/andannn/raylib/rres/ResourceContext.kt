/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.rres

import io.github.andannn.raylib.base.Image
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextProvider
import io.github.andannn.raylib.core.DisposableRegistry
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.find
import kotlinx.cinterop.CValue
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.posix.free


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
