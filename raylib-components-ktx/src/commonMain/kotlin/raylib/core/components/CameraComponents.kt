package raylib.core.components

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Camera2D
import raylib.core.ComponentsRegisterScope
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.add
import raylib.core.length
import raylib.core.scale
import raylib.core.subtract
import raylib.core.worldToScreenPosition
import kotlin.math.max

fun ComponentsRegisterScope.cameraFollowTarget(
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

fun ComponentsRegisterScope.cameraFollowTargetSmooth(
    camera: Camera2D,
    target: Vector2,
    minSpeed: Float = 30f,
    minEffectLength: Int = 10,
    fractionSpeed: Float = 0.8f,
) {
    component("followTargetSmooth") {
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

fun ComponentsRegisterScope.cameraFollowTargetCenterClamped(
    camera: Camera2D,
    target: Vector2,
    worldRect: CValue<Rectangle>
) {
    data class RectMinMax(
        val maxX: Float,
        val maxY: Float,
        val minX: Float,
        val minY: Float
    )
    component("followPlayerCenterClampedCamera") {
        provideHandlers {
            onUpdate {
                camera.target.x = target.x
                camera.target.y = target.y
                camera.offset.x = screenWidth / 2.0f
                camera.offset.y = screenHeight / 2.0f

                val (maxX, maxY, minX, minY) = worldRect.useContents {
                    RectMinMax(minX = x, maxX = x + width, minY = y, maxY = y + height)
                }
                val cameraValue = camera.readValue()
                val maxScreen = cameraValue.worldToScreenPosition(Vector2(maxX, maxY))
                val minScreen = cameraValue.worldToScreenPosition(Vector2(minX, minY))
                val (maxScreenX, maxScreenY) = maxScreen.useContents { x to y }
                val (minScreenX, minScreenY) = minScreen.useContents { x to y }
                if (maxScreenX < screenWidth) {
                    camera.offset.x = screenWidth - (maxScreenX - screenWidth / 2f)
                }
                if (maxScreenY < screenHeight) {
                    camera.offset.y = screenHeight - (maxScreenY - screenHeight / 2f)
                }
                if (minScreenX > 0) {
                    camera.offset.x = screenWidth / 2f - minScreenX
                }
                if (minScreenY > 0) {
                    camera.offset.y = screenHeight / 2f - minScreenY
                }
            }
        }
    }
}
