package raylib.core

import kotlinx.cinterop.CValue

fun DisposableRegistry.loadRenderTexture(
    width: Int,
    height: Int,
): CValue<RenderTexture2D> {
    val texture = raylib.interop.LoadRenderTexture(width, height)
    disposeOnClose {
        raylib.interop.UnloadRenderTexture(texture)
    }
    return texture
}

fun DisposableRegistry.loadRenderTexture(
    fileName: String,
): CValue<Texture> {
    val texture = raylib.interop.LoadTexture(fileName)
    disposeOnClose {
        raylib.interop.UnloadTexture(texture)
    }
    return texture
}
