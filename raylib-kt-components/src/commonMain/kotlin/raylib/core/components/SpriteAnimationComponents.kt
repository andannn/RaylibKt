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
import raylib.core.nativeStateOf
import raylib.core.suspendingTask
import raylib.easings.awaitDuration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Coroutine-driven spritesheet animation.
 *
 * @param texture The spritesheet texture.
 * @param frameCount Total number of frames.
 * @param framesSpeed Playback speed (FPS). Reactive via [State].
 * @param dest Position and scale on screen.
 * @param tag Unique ID to persist state (current frame) and prevent collisions.
 * @param origin Pivot point for rotation and scaling.
 */
fun ComponentRegistry.spriteAnimationComponent(
    texture: CValue<Texture2D>,
    frameCount: Int,
    framesSpeed: State<Int>,
    dest: Rectangle,
    tag: String = "spriteAnimation",
    origin: CValue<Vector2> = Vector2(),
) {
    component("spriteAnimation_$tag") {
        val (textureWidth, textureHeight) = texture.useContents { width to height }
        val frameRec by nativeStateOf {
            RectangleAlloc(
                0.0f,
                0.0f,
                textureWidth.toFloat() / frameCount,
                textureHeight.toFloat()
            )
        }
        var currentFrame = 0

        suspendingTask {
            while (true) {
                awaitDuration(1f.div(framesSpeed.value).times(1000).toInt().milliseconds)
                currentFrame = (currentFrame + 1) % frameCount
                frameRec.x = currentFrame.toFloat() * frameRec.width
            }
        }

        onDraw {
            drawTexture(texture, frameRec.readValue(), dest.readValue(), origin, WHITE)
        }
    }
}
