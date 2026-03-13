/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.base.Colors.WHITE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.RectangleAlloc
import io.github.andannn.raylib.core.State
import io.github.andannn.raylib.base.Texture2D
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.rememberSuspendingTask
import io.github.andannn.raylib.core.setValue
import io.github.andannn.easings.awaitDuration
import kotlin.time.Duration.Companion.milliseconds

typealias SpriteGrid = Pair<Int, Int>

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
    crossinline onFrame: (Int) -> Unit = {},
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
    var currentFrame by remember {
        mutableStateOf(0)
    }

    rememberSuspendingTask {
        while (true) {
            val speed = framesSpeed.value.coerceAtLeast(1)
            val frameDurationMs = (1000 / speed).toLong()

            onFrame(currentFrame)

            awaitDuration(frameDurationMs.milliseconds)
            currentFrame = (currentFrame + 1)
            if (currentFrame >= frameCount) {
                currentFrame = 0
                onRestart()
            }

            frameRec.x = (currentFrame % numFramePerLine) * frameRec.width
            frameRec.y = (currentFrame / numFramePerLine).toFloat() * frameRec.height
        }
    }

    onDraw {
        drawTexture(texture, frameRec.readValue(), dest, origin, WHITE)
    }
}
