package raylib.core.components

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.ComponentRegistry
import raylib.core.Context
import raylib.core.ContextProvider
import raylib.core.Matrix
import raylib.core.MutableState
import raylib.core.Rectangle
import raylib.core.RenderPhase
import raylib.core.State
import raylib.core.Vector2
import raylib.core.Vector2Alloc
import raylib.core.WindowContext
import raylib.core.component
import raylib.core.find
import raylib.core.findOrNull
import raylib.core.format
import raylib.core.invert
import raylib.core.isCollisionWith
import raylib.core.matrixIdentity
import raylib.core.multiply
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.provide
import raylib.core.remember
import raylib.core.rlMatrix
import raylib.core.transform

class Transform2DContext(
    val matrix: CValue<Matrix>
) : Context

fun ContextProvider.worldMatrix(): CValue<Matrix> {
    return findOrNull<Transform2DContext>()?.matrix ?: matrixIdentity()
}

fun ContextProvider.isScreenPointInLocalRect(screenPoint: CValue<Vector2>, localRect: CValue<Rectangle>): Boolean {
    val world = worldMatrix()
    val invWorld = world.invert()
    val localPoint = screenPoint.transform(invWorld)
    return localPoint.isCollisionWith(localRect)
}

class Transform2D(
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
    crossinline children: ComponentRegistry.(Transform2D) -> Unit
) = component("Transform2D_$tag") {
    val positionV = position ?: remember { nativeStateOf { Vector2Alloc() } }.value
    val scaleV = scale ?: remember { nativeStateOf { Vector2Alloc(1f, 1f) } }.value
    val offsetV = offset ?: remember { nativeStateOf { Vector2Alloc() } }.value
    val angle = angle ?: remember { mutableStateOf(0f) }

    val transform2DState = remember {
        Transform2D(positionV, scaleV, offsetV, angle)
    }

    when (find<WindowContext>().renderPhase) {
        RenderPhase.UPDATE -> provide<Transform2DContext>(Transform2DContext(worldMatrix().multiply(transform2DState.toMatrix()))) {
            children(transform2DState)
        }

        RenderPhase.DRAW -> transform2DDrawInterceptor(transform2DState) {
            children(transform2DState)
        }
    }
}

inline fun transform2DDrawInterceptor(
    transform2DState: Transform2D,
    crossinline block: (CValue<Matrix>) -> Unit
): CValue<Matrix> {
    val transform = transform2DState.toMatrix()

    rlMatrix {
        multMatrix(transform)
        block(transform)
    }

    return transform
}

fun Transform2D.toMatrix(): CValue<Matrix> {
    val transform2DState: Transform2D = this
    val position: Vector2 = transform2DState.position
    val angle: State<Float> = transform2DState.angle
    val scale: Vector2 = transform2DState.scale
    val offset: Vector2 = transform2DState.offset
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
    return transform

}
