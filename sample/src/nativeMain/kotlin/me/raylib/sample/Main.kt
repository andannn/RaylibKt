package me.raylib.sample

import kotlinx.cinterop.IntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import me.raylib.sample.core.*
import me.raylib.sample.custom.*
import me.raylib.sample.shape.*
import me.raylib.sample.textures.*
import io.github.andannn.raylib.base.Colors
import io.github.andannn.raylib.base.MouseButton
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.base.isCollisionWith
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.provide
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.window
import io.github.andannn.raylib.gui.GuiContext
import io.github.andannn.raylib.gui.onDrawGui
import kotlin.experimental.ExperimentalNativeApi

enum class Example(val title: String) {
    FIRST_WINDOW("First Window"),
    DELTA_TIME("Delta Time"),
    INPUT_KEYS("Input Keys"),
    INPUT_MOUSE("Input mouse"),
    INPUT_MOUSE_WHEEL("Input Mouse Wheel"),
    INPUT_MULTITOUCH("Input Multitouch"),
    INPUT_GESTURES("Input Gestures"),
    TWO_D_CAMERA("2D Camera"),
    TWO_D_CAMERA_MOUSE_ZOOM("2D Camera mouse zoom"),
    TWO_D_CAMERA_SPLIT_SCREEN("2D Camera split screen"),
    WINDOW_SHOULD_CLOSE("Window should close"),
    WINDOW_FLAGS("Window flags"),
    MONITOR_DETECTOR("Monitor detector"),
    SCISSOR_TEST("Scissor Test"),
    BASIC_SCREEN_MANAGER("Basic Screen Manager"),
    RANDOM_SEQUENCE("Random sequence"),
    TOW_D_CAMERA_PLATFORMER("2D Camera platformer"),
    RENDER_TEXTURE("Render texture"),
    BOUNCING_BALL("Bouncing ball"),
    RANDOM_OBJECT_GENERATOR("Random object generator"),
    BASIC_SHAPES("Basic shapes"),
    RECTANGLE_SCALING("Rectangle scaling"),
    LINES_BEZIER("Lines bezier"),
    COLLISION_AREA("Collision area"),
    FOLLOWING_EYES("Following eyes"),
    EASING_BALL("Easing ball"),
    EASING_BOX("Easing box"),
    EASINGS_RECTANGLES("Easings rectangles"),
    MOUSE_TRAIL("Mouse trail"),
    RING_DRAWING("Ring drawing"),
    INPUT_ACTIONS("Input actions"),
    LOGO_RAYLIB("Logo Raylib"),
    SRCREC_DSTREC("srcrec dstrec"),
    SPRITE_ANIMATION_SAMPLE("Sprite Animation"),
    SPRITE_EXPLOSION("Sprite explosion"),
    MATRIX_TEST("Matrix test"),
    IMAGE_DRAWING("Image drawing"),
}

@OptIn(ExperimentalNativeApi::class)
@CName(externName = "raylib_android_main")
fun main() = window(
    title = "raylib [core] example - input gestures",
    width = 800,
    height = 450,
    initialBackGroundColor = Colors.RAYWHITE,
) {
    provide(GuiContext()) {
        val active = remember {
            nativeStateOf { alloc<IntVar> { value = -1 } }
        }

        val currentExample = remember {
            mutableStateOf<Example?>(Example.entries.last())
        }

        component("menu_control") {
            onUpdate {
                if (active.value.value != -1) {
                    currentExample.value = Example.entries[active.value.value]
                } else {
                    currentExample.value = null
                }
            }
        }

        when (currentExample.value) {
            null -> {
                component("menu") {
                    val bounds = remember {
                        Rectangle(0f, 0f, 400f, screenHeight.toFloat())
                    }
                    val scrollIndex by remember {
                        nativeStateOf { alloc<IntVar> {} }
                    }
                    onDrawGui {
                        guiListView(
                            bounds = bounds,
                            text = Example.entries.joinToString(";") { it.title },
                            scrollIndex = scrollIndex.ptr,
                            active = active.value.ptr
                        )
                    }
                }
            }

            Example.FIRST_WINDOW -> firstWindow()
            Example.DELTA_TIME -> deltaTime()
            Example.INPUT_KEYS -> inputKeys()
            Example.INPUT_MOUSE -> inputMouse()
            Example.INPUT_MOUSE_WHEEL -> inputMouseWheel()
            Example.INPUT_MULTITOUCH -> inputMultitouch()
            Example.INPUT_GESTURES -> inputGestures()
            Example.TWO_D_CAMERA -> twoDCamera()
            Example.TWO_D_CAMERA_MOUSE_ZOOM -> twoDCameraMouseZoom()
            Example.TWO_D_CAMERA_SPLIT_SCREEN -> twoDCameraSplitScreen()
            Example.WINDOW_SHOULD_CLOSE -> windowShouldClose()
            Example.WINDOW_FLAGS -> windowFlags()
            Example.MONITOR_DETECTOR -> monitorDetector()
            Example.SCISSOR_TEST -> scissorTest()
            Example.BASIC_SCREEN_MANAGER -> basicScreenManager()
            Example.RANDOM_SEQUENCE -> randomSequence()
            Example.TOW_D_CAMERA_PLATFORMER -> towDCameraPlatformer()
            Example.RENDER_TEXTURE -> renderTexture()
            Example.BOUNCING_BALL -> bouncingBall()
            Example.RANDOM_OBJECT_GENERATOR -> randomObjectGenerator()
            Example.BASIC_SHAPES -> basicShapes()
            Example.RECTANGLE_SCALING -> rectangleScaling()
            Example.LINES_BEZIER -> linesBezier()
            Example.COLLISION_AREA -> collisionArea()
            Example.FOLLOWING_EYES -> followingEyes()
            Example.EASING_BALL -> easingBall()
            Example.EASING_BOX -> easingBox()
            Example.EASINGS_RECTANGLES -> easingsRectangles()
            Example.MOUSE_TRAIL -> mouseTrail()
            Example.RING_DRAWING -> ringDrawing()
            Example.INPUT_ACTIONS -> inputActions()
            Example.LOGO_RAYLIB -> logoRaylib()
            Example.SRCREC_DSTREC -> srcrecDstrec()
            Example.SPRITE_ANIMATION_SAMPLE -> spriteAnimationSample()
            Example.SPRITE_EXPLOSION -> spriteExplosion()
            Example.MATRIX_TEST -> matrixTest()
            Example.IMAGE_DRAWING -> imageDrawing()
        }

        if (currentExample.value != null) {
            component("back") {
                val center = remember { Vector2(screenWidth - 20f, 20f) }
                val radius = 15f
                onUpdate {
                    if (MouseButton.MOUSE_BUTTON_LEFT.isPressed() && mousePosition.isCollisionWith(center, radius)) {
                        currentExample.value = null
                        active.value.value = -1
                    }
                }
                onDraw {
                    drawCircle(center, radius, Colors.RED)
                }
            }
        }
    }
}
