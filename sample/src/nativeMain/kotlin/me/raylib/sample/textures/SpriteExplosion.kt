package me.raylib.sample.textures

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import raylib.core.ComponentRegistry
import raylib.core.MouseButton
import raylib.core.Rectangle
import raylib.core.RectangleAlloc
import raylib.core.Vector2
import raylib.core.components.spriteAnimationComponent
import raylib.core.loadTexture
import raylib.core.mutableStateListOf
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf

fun ComponentRegistry.spriteExplosion() {
    val explosion = remember("explosion") {
        loadTexture("resources/explosion.png")
    }
    val explosionContainer = remember("explosionContainer") {
        mutableStateListOf<ExplosionState>()
    }

    component("mouse click") {
        var id = 0L
        onUpdate {
            if (MouseButton.MOUSE_BUTTON_LEFT.isPressed()) {
                explosionContainer.addState(nativeStateOf { ExplosionState(id = id++,
                    RectangleAlloc(mouseX.toFloat(), mouseY.toFloat(), 100f, 100f)
                    ) })
            }
        }
    }

    explosionContainer.forEach {
        spriteAnimationComponent(
            texture = explosion,
            spriteGrid = 5 to 5,
            framesSpeed = mutableStateOf(8),
            dest = it.value.rect,
            tag = "explosion_${it.value.id}"
        )
    }
}

class ExplosionState(
     val id: Long,
     val rect: Rectangle
)