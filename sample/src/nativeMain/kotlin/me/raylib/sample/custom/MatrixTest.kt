package me.raylib.sample.custom

import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Colors.SKYBLUE
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.randomColor
import io.github.andannn.raylib.components.Positional2DEntity
import io.github.andannn.raylib.components.Positional2D
import io.github.andannn.raylib.components.collision2DComponent
import io.github.andannn.raylib.components.positional2DAlloc
import io.github.andannn.raylib.components.positional2DComponent
import io.github.andannn.raylib.components.hitTest
import io.github.andannn.raylib.components.positional2DEntityComponent
import io.github.andannn.raylib.components.queryNearby
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.matrixTest() {
    collision2DComponent("matrixTest", cellSize = 50) {
        val positional2D = remember {
            positional2DAlloc(
                size = Vector2(50f, 50f),
                offset = Vector2(-25f, -25f)
            )
        }

        component("update") {
            onUpdate { dt ->
                val transform = positional2D.transform
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

        val someHitbox by remember {
            nativeStateOf {
                SomeHitbox(state = positional2DAlloc(size = Vector2(30f, 30f)))
            }
        }
        positional2DEntityComponent(
            someHitbox,
            tag = "Collision"
        ) {
        }

        positional2DComponent(
            state = positional2D,
            size = Vector2(50f, 50f)
        ) {
            someItemGroup(positional2D)
        }
    }
}

const val speed = 200f

private fun ComponentRegistry.someItemGroup(positional2D: Positional2D) = component("content") {
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

        positional2D.queryNearby { identity ->
            println("asdfadsf $identity")
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

class SomeHitbox(override val state: Positional2D) : Positional2DEntity {

}