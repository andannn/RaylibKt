/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import io.github.andannn.raylib.tiled.TiledMapFiles
import io.github.andannn.raylib.tiled.TiledMapProvider
import kotlin.test.Test
import kotlin.test.assertEquals

class TiledMapProviderTest {

    @Test
    fun tiledMapProviderTest_getMap() {
        val provider = TiledMapProvider(object : TiledMapFiles {
            override fun tmJsonFile(): String {
                return "src/commonTest/dummy/test.tmj"
            }

            override fun txJsonFiles(): List<String> {
                return listOf(
                    "src/commonTest/dummy/Confetti (16x16).tsj",
                    "src/commonTest/dummy/Terrain (16x16).tsj",
                )
            }
        })

        val tileset0 = provider.getMap().tilesets[0]
        val tileset1 = provider.getMap().tilesets[1]
        assertEquals(1, tileset0.firstGid)
        assertEquals(16, tileset0.imageHeight)
        assertEquals(96, tileset0.imageWidth)
        assertEquals(7, tileset1.firstGid)
        assertEquals(176, tileset1.imageHeight)
        assertEquals(352, tileset1.imageWidth)
    }

    @Test
    fun tiledMapProviderTest_build_provider() {
        val provider = TiledMapProvider("src/commonTest/dummy/test.tmj")
        val tileset0 = provider.getMap().tilesets[0]
        assertEquals(1, tileset0.firstGid)
        assertEquals(16, tileset0.imageHeight)
        assertEquals(96, tileset0.imageWidth)
    }
}