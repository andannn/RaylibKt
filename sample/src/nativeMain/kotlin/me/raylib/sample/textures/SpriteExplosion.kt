package me.raylib.sample.textures

import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.RectangleAlloc
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.components.spriteAnimationComponent
import io.github.andannn.raylib.core.loadTexture
import io.github.andannn.raylib.core.mutableStateListOf
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember

fun ComponentRegistry.spriteExplosion() {
    val explosion = remember {
        loadTexture("resources/explosion.png")
    }
    val explosionContainer = remember {
        mutableStateListOf<ExplosionState>()
    }

    component("mouse click") {
        var id = 0L
        onUpdate {
            if (MouseButton.MOUSE_BUTTON_LEFT.isPressed()) {
                explosionContainer.addState {
                    nativeStateOf {
                        ExplosionState(
                            id = id++,
                            RectangleAlloc(mouseX.toFloat(), mouseY.toFloat(), 100f, 100f)
                        )
                    }
                }
            }
        }
    }

    explosionContainer.forEach {
        spriteAnimationComponent(
            texture = explosion,
            spriteGrid = 5 to 5,
            framesSpeed = mutableStateOf(12),
            dest = it.value.rect,
            origin = Vector2(50f, 50f),
            tag = "explosion_${it.value.id}",
            onRestart = { it.dispose() }
        )
    }
}

class ExplosionState(
    val id: Long,
    val rect: Rectangle
)