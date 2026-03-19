package io.github.andannn.raylib.rres

import io.github.andannn.raylib.core.Context
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.posix.free


interface ResourceContext : Context, RresFunction, RaylibRresFunction

fun ResourceContext(
    rresFunction: RresFunction = RresFunction(),
    raylibRresFunction: RaylibRresFunction = RaylibRresFunction(),
): ResourceContext = object : ResourceContext,
    RresFunction by rresFunction,
    RaylibRresFunction by raylibRresFunction {}

inline fun ResourceContext.traverseResourceChunkInfo(
    fileName: String,
    block: (ResourceChunkInfo) -> Unit
) = memScoped {
    val count = alloc<UIntVar>()
    println("count ${count.value}")
    val infos = loadResourceChunkInfoAll(fileName, count.ptr)?: return@memScoped
    for (i in 0 until count.value.toInt()) {
        block(infos[i])
    }
    free(infos)
}
