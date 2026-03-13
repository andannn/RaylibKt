package me.raylib.sample.textures

import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.components.spriteAnimationComponent
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.components
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.loadTexture
import io.github.andannn.raylib.core.mutableStateListOf
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue
import kotlinx.cinterop.CValue

fun ComponentRegistry.spriteExplosion() = component("explosion") {
    val explosion = remember {
        loadTexture("resources/explosion.png")
    }
    val explosionContainer = remember {
        mutableStateListOf<ExplosionState>()
    }

    component("mouse click") {
        var id by remember {
            mutableStateOf(0L)
        }
        onUpdate {
            if (MouseButton.MOUSE_BUTTON_LEFT.isPressed()) {
                explosionContainer.addState {
                    ExplosionState(
                        id = id++,
                        Rectangle(mouseX.toFloat(), mouseY.toFloat(), 100f, 100f)
                    )
                }
            }
        }
    }

    components(
        items = explosionContainer,
        key = { item -> "explosion_${item.id}" }
    ) { state ->
        spriteAnimationComponent(
            texture = explosion,
            spriteGrid = 5 to 5,
            framesSpeed = mutableStateOf(12),
            dest = state.value.rect,
            origin = Vector2(50f, 50f),
            key = "explosion",
            onRestart = { state.dispose() }
        )
    }
}

class ExplosionState(
    val id: Long,
    val rect: CValue<Rectangle>
)