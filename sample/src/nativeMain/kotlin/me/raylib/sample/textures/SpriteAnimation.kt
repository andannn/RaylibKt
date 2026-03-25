package me.raylib.sample.textures

import io.github.andannn.raylib.assets.fileResourceResolver
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.foundation.Colors.DARKGRAY
import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.LIME
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.components.spriteAnimationComponent
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember

private const val MAX_FRAME_SPEED = 15
private const val MIN_FRAME_SPEED = 1

fun ComponentRegistry.spriteAnimationSample() {
    val scarfy = remember {
        fileResourceResolver.resolveImageTexture("resources/scarfy.png")
    }

    val framesSpeed = remember {
        mutableStateOf(8)
    }

    component("spriteAnimationSampleControl") {
        update {
            if (KeyboardKey.KEY_RIGHT.isPressed()) {
                framesSpeed.value = (framesSpeed.value + 1).coerceIn(MIN_FRAME_SPEED, MAX_FRAME_SPEED)
            }
            if (KeyboardKey.KEY_LEFT.isPressed()) {
                framesSpeed.value = (framesSpeed.value - 1).coerceIn(MIN_FRAME_SPEED, MAX_FRAME_SPEED)
            }
        }
    }

    val scarfyRect = remember {
        val (scarfyWidth, scarfyHeight) = scarfy.useContents { width.toFloat().div(6f) to height.toFloat() }
        Rectangle(350.0f, 280.0f, scarfyWidth, scarfyHeight)
    }

    spriteAnimationComponent(
        key = "",
        texture = scarfy,
        spriteGrid = 6 to 1,
        framesSpeed = framesSpeed,
        dest = scarfyRect,
    )

    component("text info") {
        draw {
            for (i in 0..<MAX_FRAME_SPEED) {
                if (i < framesSpeed.value) drawRectangle(250 + 21 * i, 205, 20, 20, RED)
                drawRectangleLines(250 + 21 * i, 205, 20, 20, MAROON)
            }

            drawTexture(scarfy, 15, 40, WHITE)
            drawRectangleLines(15, 40, scarfy.useContents { width }, scarfy.useContents { height }, LIME)
            drawText("FRAME SPEED: ", 165, 210, 10f, DARKGRAY)
            drawText("${framesSpeed.value} FPS", 575, 210, 10f, DARKGRAY)
            drawText("PRESS RIGHT/LEFT KEYS to CHANGE SPEED!", 290, 240, 10f, DARKGRAY)
            drawText("(c) Scarfy sprite by Eiden Marsal", screenWidth - 200, screenHeight - 20, 10f, GRAY);
        }
    }
}

