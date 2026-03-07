package raylib.core.components

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.Rectangle
import raylib.core.RectangleAlloc
import raylib.core.State
import raylib.core.Texture2D
import raylib.core.Vector2
import raylib.core.getValue
import raylib.core.nativeStateOf
import raylib.core.remember
import raylib.core.suspendingTask
import raylib.easings.awaitDuration
import kotlin.time.Duration.Companion.milliseconds

typealias SpriteGrid = Pair<Int, Int>

/**
 * Coroutine-driven spritesheet animation.
 *
 * @param texture The spritesheet texture.
 * @param spriteGrid The grid layout of the spritesheet (columns to rows).
 * @param framesSpeed Playback speed (FPS). Reactive via [State].
 * @param dest Position and scale on screen.
 * @param tag Unique ID to persist state (current frame) and prevent collisions.
 * @param origin Pivot point for rotation and scaling.
 */
fun ComponentRegistry.spriteAnimationComponent(
    texture: CValue<Texture2D>,
    spriteGrid: SpriteGrid,
    framesSpeed: State<Int>,
    dest: Rectangle,
    origin: CValue<Vector2> = Vector2(),
    tag: String = "spriteAnimation",
    onRestart: () -> Unit = {},
) {
    component("spriteAnimation_$tag") {
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
        var currentFrame = 0

        suspendingTask {
            while (true) {
                val speed = framesSpeed.value.coerceAtLeast(1)
                val frameDurationMs = (1000 / speed).toLong()
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
            drawTexture(texture, frameRec.readValue(), dest.readValue(), origin, WHITE)
        }
    }
}
