package me.raylib.sample.custom

import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Colors.SKYBLUE
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.randomColor
import io.github.andannn.raylib.components.Spatial2DBoxStateAlloc
import io.github.andannn.raylib.components.box2DComponent
import io.github.andannn.raylib.components.hitTest
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.matrixTest() {
    component("matrixTest") {
        val spatialState = remember {
            Spatial2DBoxStateAlloc(
                offset = Vector2(-25f, -25f)
            )
        }

        component("update") {
            onUpdate { dt ->
                val transform = spatialState.transform
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
        }

        box2DComponent(
            state = spatialState,
            size = Vector2(50f, 50f)
        ) {
            someItemGroup()
        }
//        transform2DComponent(
//            tag = "test",
//            transform = spatialState.transform
//        ) {
//            val transform2 = remember {
//                Transform2DAlloc(position = Vector2(100f, 100f), angle = mutableStateOf(45f))
//            }
//            transform2DComponent(
//                transform = transform2
//            ) {
//                someItemGroup()
//                aabbComponent(aabb, Vector2(50f, 50f), tag = "rect")
//            }
//        }
    }
}

const val speed = 200f

private fun ComponentRegistry.someItemGroup() = component("content") {
    var randomColor by remember {
        mutableStateOf(RED)
    }
    val rect = remember {
        Rectangle(0f, 0f, 50f, 50f)
    }
    onUpdate {
        if (MouseButton.MOUSE_BUTTON_LEFT.isPressed() &&
            mousePosition.hitTest(rect)
        ) {
            randomColor = randomColor()
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
