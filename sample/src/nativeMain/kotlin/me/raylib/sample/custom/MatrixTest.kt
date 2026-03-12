package me.raylib.sample.custom

import io.github.andannn.raylib.base.Colors.BLUE
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.components.Anchor
import io.github.andannn.raylib.components.Entity
import io.github.andannn.raylib.components.Spatial2DAlloc
import io.github.andannn.raylib.components.queryAABBCollision
import io.github.andannn.raylib.components.registerEntityToWorldGrid2D
import io.github.andannn.raylib.components.spatial2DComponent
import io.github.andannn.raylib.components.world2DGridComponent
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.useContents

fun ComponentRegistry.matrixTest() {
    world2DGridComponent("matrixTest", cellSize = 50) {
        val spatial2D = remember {
            Spatial2DAlloc(
                size = Vector2(50f, 50f),
                scale = Vector2(2f, 2f),
                anchor = Anchor.CENTER
            )
        }

        component("update") {
            onUpdate { dt ->
                val transform = spatial2D.transform
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

        spatial2DComponent(
            "some item",
            state = spatial2D,
        ) {
            onDraw {
                val rect = spatial2D.size.useContents { Rectangle(width = x, height = y) }
                drawRectangle(rect, BLUE)
            }

            someHitbox {
                println("hit ")
            }
        }

        val hitbox2 = remember {
            HitBox2()
        }
        spatial2DComponent(
            "some item2",
            size = Vector2(100f, 100f),
            position = Vector2(200f, 200f)
        ) {
            registerEntityToWorldGrid2D(hitbox2, it)
        }
    }
}

const val speed = 200f

private inline fun ComponentRegistry.someHitbox(
    crossinline onHit: () -> Unit
) = component("content") {
    val someHitbox = remember {
        HitBox1()
    }

    spatial2DComponent(
        "hitbox1",
        size = Vector2(25f, 25f),
        anchor = Anchor.CENTER
    ) { position ->
        registerEntityToWorldGrid2D(someHitbox, position)

        onUpdate {
            position.queryAABBCollision<HitBox2> { hitbox, d, any ->
                onHit()
            }
        }
    }
}

class HitBox1() : Entity {

}

class HitBox2() : Entity {

}