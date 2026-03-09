package me.raylib.sample.shape

import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.RectangleAlloc
import io.github.andannn.raylib.base.Vector2Alloc
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.rememberSuspendingTask
import io.github.andannn.raylib.core.setValue
import io.github.andannn.easings.Ease
import io.github.andannn.easings.animateTo
import io.github.andannn.easings.awaitDuration
import io.github.andannn.easings.awaitEasingAnimation
import raylib.interop.Fade
import kotlin.time.Duration.Companion.seconds

fun ComponentRegistry.easingBox() {
    component("A") {
        val rec by remember {
            nativeStateOf { RectangleAlloc(screenWidth / 2.0f, -100f, 100f, 100f) }
        }
        var rotation by remember {
            mutableStateOf(0.0f)
        }
        var alpha by remember {
            mutableStateOf(1.0f)
        }

        val animationTaskController = rememberSuspendingTask {
            rec.x = screenWidth / 2.0f
            rec.y = -100f
            rec.width = 100f
            rec.height = 100f
            rotation = 0.0f
            alpha = 1.0f
            awaitEasingAnimation(
                rec.y,
                target = screenHeight.div(2f),
                duration = 2.seconds,
                easing = Ease.ElasticOut,
                onUpdate = { rec.y = it }
            )

            awaitDuration(2.seconds) { fraction ->
                rec.height = 100f.animateTo(10f, fraction, Ease.BounceOut)
                rec.width = 100f.animateTo(100f + screenWidth, fraction, Ease.BounceOut)
            }

            awaitDuration(4.seconds) { fraction ->
                rotation = 0f.animateTo(270f, fraction, Ease.QuadOut)
            }

            awaitDuration(2.seconds) { fraction ->
                rec.height = 0f.animateTo(10f + screenWidth, fraction, Ease.CircOut)
            }

            awaitDuration(2.8.seconds) { fraction ->
                alpha = 1f.animateTo(0f, fraction, Ease.SineOut)
            }
        }

        onUpdate {
            if (KeyboardKey.KEY_SPACE.isPressed()) {
                animationTaskController.start()
            }
        }

        onDraw {
            memScoped {
                drawRectangle(
                    rectangle = rec.readValue(),
                    origin = Vector2Alloc(rec.width / 2, rec.height / 2).readValue(),
                    rotation = rotation,
                    Fade(BLACK, alpha)
                )
                drawText("PRESS [SPACE] TO RESET BOX ANIMATION!", 10, screenHeight - 25, 20, LIGHTGRAY)
            }
        }
    }
}
