package me.raylib.sample.shape

import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.gui.drawGui
import raylib.interop.Fade

fun ComponentRegistry.ringDrawing() {
    component("A") {
        val startAngle by remember {
            nativeStateOf { alloc<FloatVar>() }
        }
        val endAngle by remember {
            nativeStateOf { alloc<FloatVar>() }
        }
        val drawRing by remember {
            nativeStateOf { alloc<BooleanVar>() }
        }
        val drawRingLines by remember {
            nativeStateOf { alloc<BooleanVar>() }
        }
        val drawCircleLines by remember {
            nativeStateOf { alloc<BooleanVar>() }
        }
        val innerRadius by remember {
            nativeStateOf { alloc<FloatVar> { value = 80f } }
        }
        val outerRadius by remember {
            nativeStateOf { alloc<FloatVar> { value = 190f } }
        }
        val segments by remember { nativeStateOf { alloc<FloatVar> {} } }
        val center = Vector2((screenWidth - 300) / 2.0f, screenHeight / 2.0f)

        drawGui {
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
