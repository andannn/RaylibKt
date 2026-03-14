package me.raylib.sample.custom

import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.tiled.component.tiledComponent

fun ComponentRegistry.tileMapTest() = component("tile-mapTest") {
    tiledComponent("", "resources/tiled/test.tmj")
}