/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core.components

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.core.Color
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.DrawContext
import raylib.core.Rectangle
import raylib.core.RenderPhase
import raylib.core.RenderTexture
import raylib.core.RenderTexture2D
import raylib.core.State
import raylib.core.Texture
import raylib.core.TextureDrawFunction
import raylib.core.Vector2
import raylib.core.WindowContext
import raylib.core.component
import raylib.core.find
import raylib.core.loadRenderTexture
import raylib.core.mutableStateOf
import raylib.core.remember
import raylib.core.textureDrawScope

/**
 * Creates a component that renders its children into an off-screen [RenderTexture2D].
 *
 * @param width The pixel width of the off-screen buffer.
 * @param height The pixel height of the off-screen buffer.
 * @param dstRectangle The destination area on the screen where the texture will be drawn.
 * @param origin The center of rotation and scaling within the destination rectangle.
 * @param rotation A [State] holding the rotation angle in degrees.
 * @param tint The color tint applied to the texture when drawing to the screen.
 * @param backgroundColor The clear color used for the render texture at the start of each frame.
 * @param children DSL block to define components that will be rendered inside this texture.
 */
inline fun ComponentRegistry.fixSizedTextureComponent(
    width: Int,
    height: Int,
    dstRectangle: Rectangle,
    origin: CValue<Vector2> = Vector2(),
    rotation: State<Float> = mutableStateOf(0f),
    tint: CValue<Color> = WHITE,
    backgroundColor: CValue<Color> = WHITE,
    tag: String = "",
    crossinline children: ComponentRegistry.(CValue<Texture>) -> Unit
) = component("fixed_size_texture_$tag") {
    val loadedTexture = remember {
        loadRenderTexture(width, height)
    }
    val sourceRect = remember {
        loadedTexture.useContents {
            Rectangle(0f, 0f, texture.width.toFloat(), -texture.height.toFloat())
        }
    }
    val texture = loadedTexture.useContents {
        texture.readValue()
    }
    if (find<WindowContext>().renderPhase == RenderPhase.DRAW) {
        textureDrawInterceptor(
            find<DrawContext>(),
            loadedTexture,
            sourceRect,
            dstRectangle,
            origin,
            rotation,
            tint,
            backgroundColor
        ) {
            children(texture)
        }
    } else {
        children(texture)
    }
}

inline fun textureDrawInterceptor(
    textureDrawFunction: TextureDrawFunction,
    loadedTexture: CValue<RenderTexture>,
    sourceRect: CValue<Rectangle>,
    dstRectangle: Rectangle,
    origin: CValue<Vector2>,
    rotation: State<Float>,
    tint: CValue<Color>,
    backgroundColor: CValue<Color>,
    crossinline block: () -> Unit
) {
    val filledTexture = textureDrawScope(
        loadedTexture,
        backgroundColor
    ) {
        block()
    }

    textureDrawFunction.drawTexture(
        filledTexture.useContents { texture.readValue() },
        sourceRect,
        dstRectangle.readValue(),
        origin,
        tint,
        rotation.value
    )
}
