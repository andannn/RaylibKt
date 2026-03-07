package me.raylib.sample.shape

import kotlinx.cinterop.CValue
import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Colors.BLUE
import raylib.core.Colors.GRAY
import raylib.core.Colors.RED
import raylib.core.ComponentRegistry
import raylib.core.MouseButton
import raylib.core.Vector2
import raylib.core.getValue
import raylib.core.isCollisionWith
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.remember
import raylib.core.setValue

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
        onUpdate {
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

        onDraw {
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
