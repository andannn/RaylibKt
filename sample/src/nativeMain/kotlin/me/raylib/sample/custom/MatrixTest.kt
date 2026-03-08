package me.raylib.sample.custom

import raylib.core.Colors.RED
import raylib.core.Colors.SKYBLUE
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.MouseButton
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.Vector2Alloc
import raylib.core.component
import raylib.core.components.Transform2D
import raylib.core.components.Transform2DAlloc
import raylib.core.components.hitTest
import raylib.core.components.transform2DComponent
import raylib.core.getValue
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.randomColor
import raylib.core.remember
import raylib.core.rlMatrix
import raylib.core.setValue

fun ComponentRegistry.matrixTest() {
    component("matrixTest") {
        val transform = remember {
            Transform2DAlloc(position = Vector2(-25f, -25f))
        }
        onDraw {
            rlMatrix {
                translate(0f, 25 * 50f, 0f)
                rotate(90f, 1f, 0f, 0f)
                drawGrid(100, 50f)
            }
        }
        transform2DComponent(
            tag = "test",
            transform2D = transform
        ) { transformBox ->
            onUpdate {
                transformBox.scale.x -= 0.01f
            }
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
            mousePosition.hitTest(rect)
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
