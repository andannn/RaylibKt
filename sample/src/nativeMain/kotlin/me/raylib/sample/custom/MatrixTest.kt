package me.raylib.sample.custom

import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Colors.SKYBLUE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2Alloc
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.components.Transform2D
import io.github.andannn.raylib.components.hitTest
import io.github.andannn.raylib.components.transform2DComponent
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.base.randomColor
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

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
