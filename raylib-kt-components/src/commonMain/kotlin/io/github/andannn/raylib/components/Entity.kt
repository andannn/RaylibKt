package io.github.andannn.raylib.components

interface Entity {

}

interface Positional2DEntity : Entity {
    val state: Positional2D
}

fun Positional2DEntity.getRect() = state.aabb.toRect()

