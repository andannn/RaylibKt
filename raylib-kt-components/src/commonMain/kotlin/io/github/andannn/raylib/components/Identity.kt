package io.github.andannn.raylib.components

interface Identity {

}

interface Positional2DIdentity : Identity {
    val state: Positional2D
}

fun Positional2DIdentity.getRect() = state.aabb.toRect()

