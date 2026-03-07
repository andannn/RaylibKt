package me.raylib.sample.custom

import raylib.core.Colors.RED
import raylib.core.Colors.SKYBLUE
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.Rectangle
import raylib.core.Vector2Alloc
import raylib.core.components.Transform
import raylib.core.components.transform2DComponent
import raylib.core.getValue
import raylib.core.nativeStateOf
import raylib.core.remember

fun ComponentRegistry.matrixTest() {
    component("matrixTest") {
        val offset by remember {
            nativeStateOf { Vector2Alloc(-25f, -25f) }
        }
        transform2DComponent(
            tag = "test",
            offset = offset
        ) { transformBox ->
            someItemGroup(transformBox)
        }
    }
}

const val speed = 100f

private fun ComponentRegistry.someItemGroup(transform: Transform) = component("content") {
    onUpdate { dt ->
        if (KeyboardKey.KEY_RIGHT.isDown()) {
            transform.position.x += speed * dt
        }
        if (KeyboardKey.KEY_LEFT.isDown()) {
            transform.position.x -= speed * dt
        }
        if (KeyboardKey.KEY_UP.isDown()) {
            transform.position.y -= speed * dt
        }
        if (KeyboardKey.KEY_DOWN.isDown()) {
            transform.position.y += speed * dt
        }
        if (KeyboardKey.KEY_SPACE.isDown()) {
            transform.angle.value += 1f
        }
    }
    onDraw {
        drawRectangle(Rectangle(0f, 0f, 50f, 50f), color = RED)
    }

    someItemFollowParent()
}

private fun ComponentRegistry.someItemFollowParent() = component("follow parent") {
    onDraw {
        drawCircle(100, 100, 50f, SKYBLUE)
    }
}
