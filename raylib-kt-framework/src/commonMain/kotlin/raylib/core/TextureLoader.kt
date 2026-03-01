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
