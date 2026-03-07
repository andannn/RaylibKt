package me.raylib.sample.custom

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import raylib.core.Colors.RED
import raylib.core.Colors.SKYBLUE
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.MouseButton
import raylib.core.Rectangle
import raylib.core.Vector2Alloc
import raylib.core.component
import raylib.core.components.Transform2D
import raylib.core.components.Transform2DContext
import raylib.core.components.isScreenPointInLocalRect
import raylib.core.components.transform2DComponent
import raylib.core.components.worldMatrix
import raylib.core.find
import raylib.core.getValue
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.randomColor
import raylib.core.remember
import raylib.core.setValue
import raylib.interop.Color

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

const val speed = 200f

private fun ComponentRegistry.someItemGroup(transform2D: Transform2D) = component("content") {
    var randomColor by remember {
        mutableStateOf(RED)
    }
    val rect = remember {
        Rectangle(0f, 0f, 50f, 50f)
    }
    onUpdate { dt ->
        if (MouseButton.MOUSE_BUTTON_LEFT.isPressed() &&
            isScreenPointInLocalRect(mousePosition, rect)
        ) {
            randomColor = randomColor()
        }

        if (KeyboardKey.KEY_RIGHT.isDown()) {
            transform2D.position.x += speed * dt
        }
        if (KeyboardKey.KEY_LEFT.isDown()) {
            transform2D.position.x -= speed * dt
        }
        if (KeyboardKey.KEY_UP.isDown()) {
            transform2D.position.y -= speed * dt
        }
        if (KeyboardKey.KEY_DOWN.isDown()) {
            transform2D.position.y += speed * dt
        }
        if (KeyboardKey.KEY_SPACE.isDown()) {
            transform2D.angle.value += 1f
        }
    }
    onDraw {
        drawRectangle(rect, color = randomColor)
    }

    someItemFollowParent()
}

private fun ComponentRegistry.someItemFollowParent() = component("follow parent") {
    onDraw {
        drawCircle(100, 100, 50f, SKYBLUE)
    }
}
