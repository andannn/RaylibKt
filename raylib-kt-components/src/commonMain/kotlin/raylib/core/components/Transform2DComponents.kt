package raylib.core.components

import kotlinx.cinterop.cValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.ComponentRegistry
import raylib.core.DrawHandler
import raylib.core.DrawInterceptor
import raylib.core.MutableState
import raylib.core.State
import raylib.core.Vector2
import raylib.core.Vector2Alloc
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.rlMatrix
import raylib.interop.Matrix

class Transform(
    val position: Vector2,
    val scale: Vector2,
    val offset: Vector2,
    val angle: MutableState<Float>
)

/**
 * A persistent state container for 2D spatial transformations.
 *
 * This box holds stable pointers to C-interop structures, allowing child components
 * to modify the parent's transformation (position, scale, rotation, offset)
 * without triggering re-allocations or losing state between frames.
 *
 * @property position The 2D translation vector in parent/world space.
 * @property scale The scaling factor (1.0 = original size).
 * @property offset The local pivot offset. Affects the center of rotation and scaling.
 * @property angle A [MutableState] holding the rotation angle in degrees.
 */
inline fun ComponentRegistry.transform2DComponent(
    position: Vector2? = null,
    scale: Vector2? = null,
    offset: Vector2? = null,
    angle: MutableState<Float>? = null,
    tag: String = "",
    crossinline children: ComponentRegistry.(Transform) -> Unit
) = component("Transform2D_$tag") {
    val positionV = position ?: remember { nativeStateOf { Vector2Alloc() } }.value
    val scaleV = scale ?: remember { nativeStateOf { Vector2Alloc(1f, 1f) } }.value
    val offsetV = offset ?: remember { nativeStateOf { Vector2Alloc() } }.value
    val angle = angle ?: remember { mutableStateOf(0f) }

    children(
        Transform(
            positionV,
            scaleV,
            offsetV,
            angle
        )
    )

    setDrawInterceptor(
        Transform2DDrawInterceptor(
            positionV,
            angle,
            scaleV,
            offsetV
        )
    )
}

class Transform2DDrawInterceptor(
    private val position: Vector2,
    private val angle: State<Float>,
    private val scale: Vector2,
    private val offset: Vector2,
) : DrawInterceptor {
    override fun interceptDraw(handler: DrawHandler) {
        val transform = cValue<Matrix>().useContents {
            val matrix = this
            val rad = angle.value * (kotlin.math.PI.toFloat() / 180f)
            val cosA = kotlin.math.cos(rad)
            val sinA = kotlin.math.sin(rad)

            val m = matrix.apply {
                m0 = cosA * scale.x
                m1 = sinA * scale.x
                m4 = -sinA * scale.y
                m5 = cosA * scale.y
                m10 = 1f
                m15 = 1f
                m12 = position.x + m0 * offset.x + m4 * offset.y
                m13 = position.y + m1 * offset.x + m5 * offset.y
            }
            m.readValue()
        }

        rlMatrix {
            multMatrix(transform)
            handler.performDraw()
        }
    }
}

