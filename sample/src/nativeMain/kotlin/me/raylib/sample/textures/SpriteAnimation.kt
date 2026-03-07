package me.raylib.sample.textures

import kotlinx.cinterop.useContents
import raylib.core.Colors.DARKGRAY
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIME
import raylib.core.Colors.MAROON
import raylib.core.Colors.RED
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.RectangleAlloc
import raylib.core.component
import raylib.core.components.spriteAnimationComponent
import raylib.core.getValue
import raylib.core.loadTexture
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.remember

private const val MAX_FRAME_SPEED = 15
private const val MIN_FRAME_SPEED = 1

fun ComponentRegistry.spriteAnimationSample() {
    val scarfy = remember {
        loadTexture("resources/scarfy.png")
    }

    val framesSpeed = remember {
        mutableStateOf(8)
    }

    component("spriteAnimationSampleControl") {
        onUpdate {
            if (KeyboardKey.KEY_RIGHT.isPressed()) {
                framesSpeed.value = (framesSpeed.value + 1).coerceIn(MIN_FRAME_SPEED, MAX_FRAME_SPEED)
            }
            if (KeyboardKey.KEY_LEFT.isPressed()) {
                framesSpeed.value = (framesSpeed.value - 1).coerceIn(MIN_FRAME_SPEED, MAX_FRAME_SPEED)
            }
        }
    }

    val scarfyRect  by remember {
        val (scarfyWidth, scarfyHeight) = scarfy.useContents { width.toFloat().div(6f) to height.toFloat() }
        nativeStateOf { RectangleAlloc(350.0f, 280.0f, scarfyWidth, scarfyHeight) }
    }

    spriteAnimationComponent(
        texture = scarfy,
        spriteGrid = 6 to 1,
        framesSpeed = framesSpeed,
        dest = scarfyRect,
    )

    component("text info") {
        onDraw {
            for (i in 0..<MAX_FRAME_SPEED) {
                if (i < framesSpeed.value) drawRectangle(250 + 21 * i, 205, 20, 20, RED)
                drawRectangleLines(250 + 21 * i, 205, 20, 20, MAROON)
            }

            drawTexture(scarfy, 15, 40, WHITE)
            drawRectangleLines(15, 40, scarfy.useContents { width }, scarfy.useContents { height }, LIME)
            drawText("FRAME SPEED: ", 165, 210, 10, DARKGRAY)
            drawText("${framesSpeed.value} FPS", 575, 210, 10, DARKGRAY)
            drawText("PRESS RIGHT/LEFT KEYS to CHANGE SPEED!", 290, 240, 10, DARKGRAY)
            drawText("(c) Scarfy sprite by Eiden Marsal", screenWidth - 200, screenHeight - 20, 10, GRAY);
        }
    }
}

