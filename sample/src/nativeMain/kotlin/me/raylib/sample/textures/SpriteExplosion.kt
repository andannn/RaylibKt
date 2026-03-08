package me.raylib.sample.textures

import kotlinx.cinterop.CValue
import raylib.core.ComponentRegistry
import raylib.core.MouseButton
import raylib.core.Rectangle
import raylib.core.RectangleAlloc
import raylib.core.Vector2
import raylib.core.component
import raylib.core.components.spriteAnimationComponent
import raylib.core.loadTexture
import raylib.core.mutableStateListOf
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.onUpdate
import raylib.core.remember

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
                            Rectangle(mouseX.toFloat(), mouseY.toFloat(), 100f, 100f)
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
    val rect: CValue<Rectangle>
)