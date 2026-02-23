package me.raylib.sample.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.plus
import kotlinx.cinterop.pointed
import kotlinx.cinterop.useContents
import kotlinx.cinterop.value
import raylib.core.Color
import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.MAROON
import raylib.core.KeyboardKey
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.WindowScope
import raylib.core.randomColor
import raylib.core.window
import raylib.interop.LoadRandomSequence
import raylib.interop.Remap
import raylib.interop.UnloadRandomSequence

fun randomSequence() {
    window(
        title = "raylib [core] example - random sequence",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        registerGameComponents {
            component("key") {
                var rectCount = 20
                var rectSize = screenWidth.toFloat() / rectCount
                var rects = generateRandomColorRectSequence(rectCount, rectSize)
                provideHandlers {
                    onUpdate {
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
                    onDraw {
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

private fun WindowScope.generateRandomColorRectSequence(rectCount: Int, rectWidth: Float): List<ColorRectWithPositon> {
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