package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.posix.expf
import platform.posix.logf
import raylib.core.Camera2D
import raylib.core.Colors.BLACK
import raylib.core.Colors.DARKGRAY
import raylib.core.Colors.MAROON
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.MouseButton
import raylib.core.Vector2
import raylib.core.add
import raylib.core.component
import raylib.core.getValue
import raylib.core.mode2d
import raylib.core.mutableStateOf
import raylib.core.rlMatrix
import raylib.core.scale
import raylib.core.screenToWorldPosition
import raylib.core.setOffset
import raylib.core.setTarget
import raylib.core.nativeStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.remember
import raylib.core.setValue
import raylib.interop.Clamp
import raylib.interop.DrawGrid
import raylib.interop.rlRotatef
import raylib.interop.rlTranslatef

fun ComponentRegistry.twoDCameraMouseZoom() {
    component("key") {
        val camera: Camera2D by remember {
            nativeStateOf { alloc<Camera2D> { zoom = 1f } }
        }
        // 0-Mouse Wheel, 1-Mouse Move
        var zoomMode by remember {
            mutableStateOf(0)
        }
        var mouseXPos by remember {
            mutableStateOf(0)
        }
        var mouseYPos by remember {
            mutableStateOf(0)
        }
        onUpdate {
            mouseXPos = mouseX
            mouseYPos = mouseY
            if (MouseButton.MOUSE_BUTTON_LEFT.isDown()) {
                val delta = mouseDelta.scale(-1.0f / camera.zoom);
                camera.setTarget(camera.target.readValue().add(delta))
            }
            if (KeyboardKey.KEY_ONE.isDown()) {
                zoomMode = 0
            } else if (KeyboardKey.KEY_TWO.isDown()) {
                zoomMode = 1
            }
            if (zoomMode == 0) {
                if (mouseWheelMove != 0f) {
                    camera.apply {
                        val wordPos = screenToWorldPosition(mousePosition)
                        val scale: Float = 0.2f * mouseWheelMove
                        zoom = Clamp(expf(logf(zoom) + scale), 0.125f, 64.0f)
                        setOffset(mousePosition)
                        setTarget(wordPos)
                    }
                }
            }
            if (zoomMode == 1) {
                if (MouseButton.MOUSE_BUTTON_RIGHT.isPressed()) {
                    camera.apply {
                        val worldPosition = screenToWorldPosition(mousePosition)
                        setOffset(mousePosition)
                        setTarget(worldPosition)
                    }
                }
                if (MouseButton.MOUSE_BUTTON_RIGHT.isDown()) {
                    val deltaX: Float = mouseDelta.useContents { x }
                    val scale = 0.005f * deltaX
                    camera.zoom = Clamp(expf(logf(camera.zoom) + scale), 0.125f, 64.0f)
                }
            }
        }

        onDraw {
            mode2d(camera) {
                rlMatrix {
                    translate(0f, 25 * 50f, 0f)
                    rotate(90f, 1f, 0f, 0f)
                    drawGrid(100, 50f)
                }
                drawCircle(screenWidth / 2, screenHeight / 2, 50f, MAROON)
            }

            drawCircle(mouseXPos, mouseYPos, 4f, DARKGRAY)
            drawText(
                "[$mouseXPos, $mouseYPos]",
                Vector2(mouseXPos.toFloat(), mouseYPos.toFloat()).add(Vector2(-44f, -24f)),
                20,
                BLACK,
                spacing = 2f
            )

            drawText("[1][2] Select mouse zoom mode (Wheel or Move)", 20, 20, 20, DARKGRAY)
            if (zoomMode == 0) {
                drawText(
                    "Mouse left button drag to move, mouse wheel to zoom",
                    20,
                    50,
                    20,
                    DARKGRAY
                )
            } else {
                drawText(
                    "Mouse left button drag to move, mouse press and move to zoom",
                    20,
                    50,
                    20,
                    DARKGRAY
                )
            }
        }
    }
}
