package me.raylib.sample.shape

import kotlinx.cinterop.CValue
import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.base.Colors.BLUE
import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.base.isCollisionWith
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.linesBezier() {
    component("k") {
        val startPoint by remember {
            nativeStateOf { alloc<Vector2> { x = 30f; y = 30f } }
        }
        val endPoint by remember {
            nativeStateOf { alloc<Vector2> { x = screenWidth - 30f; y = screenHeight - 30f } }
        }
        var moveStartPoint by remember {
            mutableStateOf(false)
        }
        var moveEndPoint by remember {
            mutableStateOf(false)
        }
        var mousePosition: CValue<Vector2>? by remember {
            mutableStateOf(null)
        }
        update {
            mousePosition = this.mousePosition
            if (mousePosition!!.isCollisionWith(
                    startPoint.readValue(),
                    10.0f
                ) && MouseButton.MOUSE_BUTTON_LEFT.isDown()
            ) {
                moveStartPoint = true
            } else if (mousePosition!!.isCollisionWith(
                    endPoint.readValue(),
                    10.0f
                ) && MouseButton.MOUSE_BUTTON_LEFT.isDown()
            ) {
                moveEndPoint = true
            }

            if (moveStartPoint) {
                startPoint.x = mousePosition!!.useContents { x }
                startPoint.y = mousePosition!!.useContents { y }
                if (MouseButton.MOUSE_BUTTON_LEFT.isReleased()) {
                    moveStartPoint = false
                }
            }
            if (moveEndPoint) {
                endPoint.x = mousePosition!!.useContents { x }
                endPoint.y = mousePosition!!.useContents { y }
                if (MouseButton.MOUSE_BUTTON_LEFT.isReleased()) {
                    moveEndPoint = false
                }
            }
        }

        draw {
            drawText("MOVE START-END POINTS WITH MOUSE", 15, 20, 20, GRAY)
            // Draw line Cubic Bezier, in-out interpolation (easing), no control points
            drawLineBezier(startPoint.readValue(), endPoint.readValue(), 4.0f, BLUE)

            // Draw start-end spline circles with some details
            drawCircle(
                startPoint.readValue(),
                if (mousePosition!!.isCollisionWith(startPoint.readValue(), 10.0f)) 14.0f else 8.0f,
                if (moveStartPoint) RED else BLUE
            )
            drawCircle(
                endPoint.readValue(),
                if (mousePosition!!.isCollisionWith(endPoint.readValue(), 10.0f)) 14.0f else 8.0f,
                if (moveEndPoint) RED else BLUE
            )
        }
    }
}
