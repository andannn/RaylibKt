package raylib.core.components

import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Camera2D
import raylib.core.ComponentsRegisterScope
import raylib.core.Vector2
import raylib.core.add
import raylib.core.length
import raylib.core.scale
import raylib.core.subtract
import kotlin.math.max

fun ComponentsRegisterScope.followTargetSmoothCamera(
    camera: Camera2D,
    target: Vector2
) {
    component("followTargetSmooth") {
        val minSpeed = 30f
        val minEffectLength = 10;
        val fractionSpeed = 0.8f;

        provideHandlers {
            onUpdate { delta ->
                camera.offset.x = screenWidth / 2.0f
                camera.offset.y = screenHeight / 2.0f
                val diff = target.readValue().subtract(camera.target.readValue())
                val length = diff.length()
                if (length > minEffectLength) {
                    val speed: Float = max(fractionSpeed * length, minSpeed)
                    camera.target.readValue().add(diff.scale(speed * delta / length)).useContents {
                        camera.target.x = x
                        camera.target.y = y
                    }
                }
            }
        }
    }
}

private fun ComponentsRegisterScope.followTargetCamera(
    camera: Camera2D,
    target: Vector2
) {
    component("followTargetCamera") {
        provideHandlers {
            onUpdate {
                camera.offset.x = screenWidth.div(2f)
                camera.offset.y = screenHeight.div(2f)
                camera.target.x = target.x
                camera.target.y = target.y
            }
        }
    }
}