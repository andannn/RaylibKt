/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.RectangleAlloc
import io.github.andannn.raylib.runtime.State
import io.github.andannn.raylib.foundation.Texture2D
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.runtime.setValue
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.rememberSuspendingTask
import io.github.andannn.raylib.runtime.awaitDuration
import kotlin.time.Duration.Companion.milliseconds

typealias SpriteGrid = Pair<Int, Int>

data class FrameInfo(
    val frameNum: Int,
    val repeatTimes: Int,
)

/**
 * Coroutine-driven spritesheet animation.
 *
 * @param key Unique ID to persist state (current frame) and prevent collisions.
 * @param texture The spritesheet texture.
 * @param spriteGrid The grid layout of the spritesheet (columns to rows).
 * @param framesSpeed Playback speed (FPS). Reactive via [State].
 * @param dest Position and scale on screen.
 * @param origin Pivot point for rotation and scaling.
 * @param onFrame called when frame start playing.
 * @param onRestart called when sprite animation play again.
 */
inline fun ComponentRegistry.spriteAnimationComponent(
    key: Any,
    texture: CValue<Texture2D>,
    spriteGrid: SpriteGrid,
    framesSpeed: State<Int>,
    dest: CValue<Rectangle>,
    origin: CValue<Vector2> = Vector2(),
    crossinline onFrame: (FrameInfo) -> Unit = {},
    crossinline onRestart: () -> Unit = {},
) = component("spriteAnimation_$key") {
    val (textureWidth, textureHeight) = texture.useContents { width to height }
    val (numFramePerLine, numLine) = spriteGrid
    val frameWidth = textureWidth.toFloat() / numFramePerLine
    val frameHeight = textureHeight.toFloat() / numLine
    val frameCount = numFramePerLine * numLine

    val frameRec by remember {
        nativeStateOf {
            RectangleAlloc(0.0f, 0.0f, frameWidth, frameHeight)
        }
    }
    var repeatTimes by remember {
        mutableStateOf(0)
    }
    var currentFrame by remember {
        mutableStateOf(0)
    }

    rememberSuspendingTask {
        while (true) {
            val speed = framesSpeed.value.coerceAtLeast(1)
            val frameDurationMs = (1000 / speed).toLong()

            onFrame(FrameInfo(currentFrame, repeatTimes))

            awaitDuration(frameDurationMs.milliseconds)
            currentFrame = (currentFrame + 1)
            if (currentFrame >= frameCount) {
                currentFrame = 0
                repeatTimes++
                onRestart()
            }

            frameRec.x = (currentFrame % numFramePerLine) * frameRec.width
            frameRec.y = (currentFrame / numFramePerLine).toFloat() * frameRec.height
        }
    }

    draw {
        drawTexture(texture, frameRec.readValue(), dest, origin, WHITE)
    }
}
