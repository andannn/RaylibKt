package me.raylib.sample.shape

import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.put
import raylib.core.stateOf
import raylib.core.window
import raylib.gui.GuiContext
import raylib.gui.onDrawGui
import raylib.interop.Fade

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
                val endAngle by stateOf { alloc<FloatVar>() }
                val drawRing by stateOf { alloc<BooleanVar>() }
                val drawRingLines by stateOf { alloc<BooleanVar>() }
                val drawCircleLines by stateOf { alloc<BooleanVar>() }
                val innerRadius by stateOf { alloc<FloatVar> { value = 80f } }
                val outerRadius by stateOf { alloc<FloatVar> { value = 190f } }
                val segments by stateOf { alloc<FloatVar> {} }
                val center = Vector2((screenWidth - 300) / 2.0f, screenHeight / 2.0f)

                onDrawGui {
                    drawLine(500, 0, 500, screenHeight, Fade(LIGHTGRAY, 0.6f))
                    drawRectangle(500, 0, screenHeight - 500, screenHeight, Fade(LIGHTGRAY, 0.3f))

                    if (drawRing.value) drawRing(
                        center,
                        innerRadius.value,
                        outerRadius.value,
                        startAngle.value,
                        endAngle.value,
                        segments.value.toInt(),
                        Fade(MAROON, 0.3f)
                    )
                    if (drawRingLines.value) drawRingLines(
                        center,
                        innerRadius.value,
                        outerRadius.value,
                        startAngle.value,
                        endAngle.value,
                        segments.value.toInt(),
                        Fade(BLACK, 0.4f)
                    )
                    if (drawCircleLines.value) drawCircleSectorLines(
                        center,
                        outerRadius.value,
                        startAngle.value,
                        endAngle.value,
                        segments.value.toInt(),
                        Fade(BLACK, 0.4f)
                    )

                    guiSliderBar(
                        bounds = Rectangle(600f, 40f, 120f, 20f),
                        textLeft = "StartAngle",
                        textRight = "${startAngle.value}",
                        value = startAngle.ptr,
                        minValue = -450f,
                        maxValue = 450f
                    )
                    guiSliderBar(
                        bounds = Rectangle(600f, 70f, 120f, 20f),
                        textLeft = "EndAngle",
                        textRight = "${endAngle.value}",
                        value = endAngle.ptr,
                        minValue = -450f,
                        maxValue = 450f
                    )

                    guiSliderBar(
                        Rectangle(600f, 140f, 120f, 20f),
                        "InnerRadius",
                        "${innerRadius.value}",
                        innerRadius.ptr,
                        0f,
                        100f
                    );
                    guiSliderBar(
                        Rectangle(600f, 170f, 120f, 20f),
                        "OuterRadius",
                        "${outerRadius.value}",
                        outerRadius.ptr,
                        0f,
                        200f
                    )
                    guiSliderBar(
                        Rectangle(600f, 240f, 120f, 20f),
                        "Segments",
                        "${segments.value}",
                        segments.ptr,
                        0f,
                        100f
                    )

                    guiCheckBox(Rectangle(600f, 320f, 20f, 20f), "Draw Ring", drawRing.ptr)
                    guiCheckBox(Rectangle(600f, 350f, 20f, 20f), "Draw RingLines", drawRingLines.ptr);
                    guiCheckBox(Rectangle(600f, 380f, 20f, 20f), "Draw CircleLines", drawCircleLines.ptr);

                    drawFPS(10, 10);
                }
            }
        }
    }
}