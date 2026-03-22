package me.raylib.sample.textures

import io.github.andannn.raylib.foundation.MouseButton
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.components.spriteAnimationComponent
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.components
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateListOf
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.assets.fileTextureAsset
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.runtime.setValue
import kotlinx.cinterop.CValue

fun ComponentRegistry.spriteExplosion() = component("explosion") {
    val explosion = remember {
        fileTextureAsset("resources/explosion.png")
    }
    val explosionContainer = remember {
        mutableStateListOf<ExplosionState>()
    }

    component("mouse click") {
        var id by remember {
            mutableStateOf(0L)
        }
        update {
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