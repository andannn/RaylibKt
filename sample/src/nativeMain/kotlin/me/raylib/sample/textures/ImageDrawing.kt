package me.raylib.sample.textures

import kotlinx.cinterop.pointed
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.foundation.Colors.DARKGRAY
import io.github.andannn.raylib.foundation.Colors.RAYWHITE
import io.github.andannn.raylib.foundation.Colors.WHITE
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.foundation.loadTextureFromImage
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.useFont
import io.github.andannn.raylib.foundation.useImage
import io.github.andannn.raylib.foundation.windowContext

fun ComponentRegistry.imageDrawing() {
    component("A") {
        val windowContext = remember { windowContext }
        val texture = remember {
            useImage(
                "resources/parrots.png",
                convert = { parrots ->
                    useImage(
                        "resources/cat.png",
                        convert = {
                            windowContext.imageCrop(it, Rectangle(100f, 10f, 280f, 380f))
                            windowContext.imageFlipHorizontal(it)
                            windowContext.imageResize(it, 150, 200)
                        }
                    ) { cat ->
                        val (catWidth, catHeight) = cat.useContents {
                            width.toFloat() to height.toFloat()
                        }
                        windowContext.imageDraw(
                            parrots,
                            cat,
                            Rectangle(0f, 0f, catWidth, catHeight),
                            Rectangle(30f, 40f, catWidth * 1.5f, catHeight * 1.5f),
                            WHITE
                        )
                    }

                    windowContext.imageCrop(
                        parrots,
                        Rectangle(0f, 50f, parrots.pointed.width.toFloat(), parrots.pointed.height.toFloat() - 100)
                    )
                    windowContext.imageDrawPixel(parrots, 10, 10, RAYWHITE)
                    windowContext.imageDrawCircleLines(parrots, 10, 10, 5, RAYWHITE)
                    windowContext.imageDrawRectangle(parrots, 5, 20, 10, 10, RAYWHITE)

                    useFont("resources/custom_jupiter_crash.png") {
                        windowContext.imageDrawTextEx(parrots, it, "PARROTS & CAT", Vector2(300f, 230f), it.useContents { baseSize.toFloat() }, -2f, WHITE);
                    }
                }
            ) {
                loadTextureFromImage(it)
            }
        }

        draw {
            val (textureWidth, textureHeight) = texture.useContents { width to height }
            drawTexture(texture, screenWidth / 2 - textureWidth / 2, screenHeight / 2 - textureHeight / 2 - 40, WHITE)
            drawRectangleLines(
                screenWidth / 2 - textureWidth / 2,
                screenHeight / 2 - textureHeight / 2 - 40,
                textureWidth,
                textureHeight,
                DARKGRAY
            )
            drawText("We are drawing only one texture from various images composed!", 240, 350, 10, DARKGRAY);
            drawText(
                "Source images have been cropped, scaled, flipped and copied one over the other.",
                190,
                370,
                10,
                DARKGRAY
            );
        }
    }
}