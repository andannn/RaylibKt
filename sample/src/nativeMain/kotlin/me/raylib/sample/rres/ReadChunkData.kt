package me.raylib.sample.rres

import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.doOnce
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.rres.ResourceContext
import kotlinx.cinterop.toKString
import rres.resources.app.AppRes.tiled_test_tmj

fun ComponentRegistry.readChunkData() = component("blend modes") {
    doOnce {
        with(find<ResourceContext>()) {
            val chunk = loadResourceChunk("app.rres", tiled_test_tmj)
            println("loadTextFromResource(chunk)?.toKString() ${loadTextFromResource(chunk)?.toKString()}")
        }
    }
}
