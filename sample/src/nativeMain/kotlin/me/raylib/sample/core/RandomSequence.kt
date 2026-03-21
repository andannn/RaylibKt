package me.raylib.sample.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.plus
import kotlinx.cinterop.pointed
import kotlinx.cinterop.useContents
import kotlinx.cinterop.value
import io.github.andannn.raylib.foundation.Color
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.foundation.WindowFunction
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.foundation.randomColor
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.setValue
import io.github.andannn.raylib.foundation.windowContext
import raylib.interop.LoadRandomSequence
import raylib.interop.Remap
import raylib.interop.UnloadRandomSequence

fun ComponentRegistry.randomSequence() {
    component("key") {
        var rectCount by remember {
            mutableStateOf(20)
        }
        var rectSize by remember {
            mutableStateOf(screenWidth.toFloat() / rectCount)
        }
        var rects by remember {
            mutableStateOf(windowContext.generateRandomColorRectSequence(rectCount, rectSize))
        }
        update {
            if (KeyboardKey.KEY_SPACE.isPressed()) {
                rects = rects.shuffled()
            }

            if (KeyboardKey.KEY_UP.isPressed()) {
                rectCount++
                rectSize = screenWidth.toFloat() / rectCount

                // Re-generate random sequence with new count
                rects = generateRandomColorRectSequence(
                    rectCount,
                    rectSize,
                )
            }
            if (KeyboardKey.KEY_DOWN.isPressed()) {
                if (rectCount >= 4) {
                    rectCount--
                    rectSize = screenWidth.toFloat() / rectCount

                    // Re-generate random sequence with new count
                    rects = generateRandomColorRectSequence(
                        rectCount,
                        rectSize,
                    )
                }
            }
        }
        draw {
            rects.forEach { rectWithPos ->
                drawRectangle(
                    Rectangle(
                        rectWithPos.position.useContents { x },
                        rectWithPos.position.useContents { y },
                        rectWithPos.colorRect.rectangleSize.useContents { x },
                        rectWithPos.colorRect.rectangleSize.useContents { y },
                    ), rectWithPos.colorRect.color
                )
            }
            drawText("Press SPACE to shuffle the current sequence", 10, screenHeight - 96, 20, BLACK);
            drawText(
                "Press UP to add a rectangle and generate a new sequence",
                10,
                screenHeight - 64,
                20,
                BLACK
            );
            drawText(
                "Press DOWN to remove a rectangle and generate a new sequence",
                10,
                screenHeight - 32,
                20,
                BLACK
            )
            drawText("Count: $rectCount rectangles", 10, 10, 20, MAROON)
            drawFPS(screenWidth - 80, 10);
        }
    }
}

class ColorRectWithPositon(
    val position: CValue<Vector2>,
    val colorRect: ColorRect
)

class ColorRect(
    val color: CValue<Color>,
    val rectangleSize: CValue<Vector2>
)

private fun WindowFunction.generateRandomColorRectSequence(
    rectCount: Int,
    rectWidth: Float
): List<ColorRectWithPositon> {
    val seq = LoadRandomSequence(rectCount.toUInt(), 0, rectCount - 1)
    val rectSeqWidth: Float = rectCount * rectWidth
    val startX = (screenWidth - rectSeqWidth) * 0.5f
    return (0..<rectCount).map { i ->
        val value = seq.plus(i)!!.pointed.value
        val height = Remap(value.toFloat(), 0f, rectCount - 1f, 0f, screenHeight.toFloat())
        ColorRectWithPositon(
            position = Vector2(startX + i * rectWidth, screenHeight - height),
            ColorRect(
                color = randomColor(),
                rectangleSize = Vector2(rectWidth, height),
            ),
        )
    }.also {
        UnloadRandomSequence(seq)
    }
}