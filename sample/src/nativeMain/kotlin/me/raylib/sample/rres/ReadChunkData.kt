package me.raylib.sample.rres

import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.doOnce
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.rres.ResourceContext
import io.github.andannn.raylib.rres.traverseResourceChunkInfo
import kotlinx.cinterop.get
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

fun ComponentRegistry.readChunkData() = component("blend modes") {
    doOnce {
//        with(find<ResourceContext>()) {
//            traverseResourceChunkInfo("resource/resources.rres") { info ->
//                println("    Resource Chunk: ${info.type.get(0)}${info.type.get(1)}${info.type.get(2)}${info.type.get(3)}")
//            }
//        }
    }
}
