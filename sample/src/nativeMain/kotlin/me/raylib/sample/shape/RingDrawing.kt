package me.raylib.sample.shape

import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import raylib.core.Colors
import raylib.core.Rectangle
import raylib.core.put
import raylib.core.stateOf
import raylib.core.window
import raylib.gui.GuiContext
import raylib.gui.onDrawGui

fun ringDrawing() {
    window(
        title = "raylib [shapes] example - ring drawing",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        put(GuiContext())

        componentRegistry {
            component("A") {
                val startAngle by stateOf { alloc<FloatVar>() }
                provideHandlers {
                    onUpdate {

                    }

                    onDrawGui {
                        guiSliderBar(
                            bounds = Rectangle(600f, 40f, 120f, 20f),
                            textLeft = "StartAngle",
                            textRight = "$startAngle",
                            value = startAngle.ptr,
                            minValue = -450f,
                            maxValue = 450f
                        )
                    }
                }
            }
        }
    }
}