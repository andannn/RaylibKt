package me.raylib.sample

import kotlinx.cinterop.IntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import me.raylib.sample.core.*
import raylib.core.Colors
import raylib.core.KeyboardKey
import raylib.core.Rectangle
import raylib.core.mutableStateOf
import raylib.core.put
import raylib.core.stateOf
import raylib.core.window
import raylib.gui.GuiContext
import raylib.gui.onDrawGui
import kotlin.experimental.ExperimentalNativeApi

enum class Example(val title: String) {
    FIRST_WINDOW("First Window"),
    DELTA_TIME("Delta Time"),
    INPUT_KEYS("Input Keys"),
    INPUT_MOUSE("Input mouse"),
}

@OptIn(ExperimentalNativeApi::class)
@CName(externName = "raylib_android_main")
fun main() {
    window(
        title = "raylib [core] example - input gestures",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        put(GuiContext())

        val currentExample = mutableStateOf<Example?>(null)
        val active by stateOf { alloc<IntVar> { value = -1 } }

        componentRegistry {
            component("menu_control") {
                onUpdate {
                    if (KeyboardKey.KEY_B.isPressed()) {
                        currentExample.value = null
                        active.value = -1
                    }
                }
                onUpdate {
                    if (active.value != -1) {
                        currentExample.value = Example.entries[active.value]
                    } else {
                        currentExample.value = null
                    }
                }
            }

            when (currentExample.value) {
                null -> {
                    component("menu") {
                        val bounds = Rectangle(0f, 0f, 100f, screenHeight.toFloat())
                        val scrollIndex by stateOf { alloc<IntVar> {} }
                        onDrawGui {
                            guiListView(
                                bounds = bounds,
                                text = Example.entries.joinToString(";") { it.name },
                                scrollIndex = scrollIndex.ptr,
                                active = active.ptr
                            )
                        }
                    }
                }
                Example.FIRST_WINDOW -> firstWindow()
                Example.DELTA_TIME -> deltaTime()
                Example.INPUT_KEYS -> inputKeys()
                Example.INPUT_MOUSE -> inputMouse()
            }
        }
    }
//    inputMouseWheel()
//    inputMultitouch()
//    inputGestures()
//    twoDCamera()
//    twoDCameraMouseZoom()
//    twoDCameraSplitScreen()
//    windowShouldClose()
//    windowFlags()
//    monitorDetector()
//    scissorTest()
//    basicScreenManager()
//    randomSequence()
//    towDCameraPlatformer()
//    renderTexture()
//    inputActions()

    // shape examples
//    bouncingBall()
//    basicShapes()
//    rectangleScaling()
//    linesBezier()
//    collisionArea()
//    followingEyes()
//    easingBall()
//    easingBox()
//    easingsRectangles()
//    mouseTrail()
//    ringDrawing()

    // custom examples
//    randomObjectGenerator()
}
