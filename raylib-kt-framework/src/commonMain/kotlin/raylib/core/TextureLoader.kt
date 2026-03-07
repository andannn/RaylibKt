package raylib.core

import kotlinx.cinterop.CValue

fun DisposableRegistry.loadRenderTexture(
    width: Int,
    height: Int,
): CValue<RenderTexture> {
    val texture = raylib.interop.LoadRenderTexture(width, height)
    disposeOnClose {
        raylib.interop.UnloadRenderTexture(texture)
    }
    return texture
}

fun DisposableRegistry.loadTexture(
    fileName: String,
): CValue<Texture> {
    val texture = raylib.interop.LoadTexture(fileName)
    disposeOnClose {
        raylib.interop.UnloadTexture(texture)
    }
    return texture
}

fun DisposableRegistry.loadTextureFromImage(
    image: CValue<Image>
): CValue<Texture2D> {
    val texture = raylib.interop.LoadTextureFromImage(image)
    disposeOnClose {
        raylib.interop.UnloadTexture(texture)
    }
    return texture
}