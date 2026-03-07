package me.raylib.sample.textures

import kotlinx.cinterop.pointed
import kotlinx.cinterop.useContents
import raylib.core.Colors.DARKGRAY
import raylib.core.Colors.RAYWHITE
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.component
import raylib.core.loadTextureFromImage
import raylib.core.onDraw
import raylib.core.remember
import raylib.core.useFont
import raylib.core.useImage

fun ComponentRegistry.imageDrawing() {
    component("A") {
        val texture = remember {
            useImage(
                "resources/parrots.png",
                convert = { parrots ->
                    useImage(
                        "resources/cat.png",
                        convert = {
                            imageCrop(it, Rectangle(100f, 10f, 280f, 380f))
                            imageFlipHorizontal(it)
                            imageResize(it, 150, 200)
                        }
                    ) { cat ->
                        val (catWidth, catHeight) = cat.useContents {
                            width.toFloat() to height.toFloat()
                        }
                        imageDraw(
                            parrots,
                            cat,
                            Rectangle(0f, 0f, catWidth, catHeight),
                            Rectangle(30f, 40f, catWidth * 1.5f, catHeight * 1.5f),
                            WHITE
                        )
                    }

                    imageCrop(
                        parrots,
                        Rectangle(0f, 50f, parrots.pointed.width.toFloat(), parrots.pointed.height.toFloat() - 100)
                    )
                    imageDrawPixel(parrots, 10, 10, RAYWHITE)
                    imageDrawCircleLines(parrots, 10, 10, 5, RAYWHITE)
                    imageDrawRectangle(parrots, 5, 20, 10, 10, RAYWHITE)

                    useFont("resources/custom_jupiter_crash.png") {
                        imageDrawTextEx(parrots, it, "PARROTS & CAT", Vector2(300f, 230f), it.useContents { baseSize.toFloat() }, -2f, WHITE);
                    }
                }
            ) {
                loadTextureFromImage(it)
            }
        }

        onDraw {
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