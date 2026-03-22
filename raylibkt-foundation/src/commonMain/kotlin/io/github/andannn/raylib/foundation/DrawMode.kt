/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import raylib.interop.BeginBlendMode
import raylib.interop.BeginMode2D
import raylib.interop.BeginScissorMode
import raylib.interop.BeginShaderMode
import raylib.interop.BeginTextureMode
import raylib.interop.ClearBackground
import raylib.interop.EndBlendMode
import raylib.interop.EndMode2D
import raylib.interop.EndScissorMode
import raylib.interop.EndShaderMode
import raylib.interop.EndTextureMode


inline fun mode2d(camera: Camera2D, crossinline block: () -> Unit) {
    mode2d(camera.readValue(), block)
}

inline fun mode2d(camera: CValue<Camera2D>, crossinline block: () -> Unit) {
    BeginMode2D(camera)
    block()
    EndMode2D()
}

inline fun scissorMode(rectangle: Rectangle, enabled: Boolean = true, crossinline block: () -> Unit) {
    scissorMode(
        rectangle.x.toInt(),
        rectangle.y.toInt(),
        rectangle.width.toInt(),
        rectangle.height.toInt(),
        enabled,
        block
    )
}

inline fun scissorMode(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    enabled: Boolean = true,
    crossinline block: () -> Unit
) {
    if (enabled) BeginScissorMode(x, y, width, height)
    block()
    if (enabled) EndScissorMode()
}

inline fun textureDrawScope(
    texture: CValue<RenderTexture>,
    backGroundColor: CValue<Color>? = null,
    crossinline block: () -> Unit
): CValue<RenderTexture> {
    BeginTextureMode(texture)
    if (backGroundColor != null) {
        ClearBackground(backGroundColor)
    }
    block()
    EndTextureMode()
    return texture
}

inline fun shaderMode(
    shader: CValue<Shader>,
    crossinline block: () -> Unit
) {
    BeginShaderMode(shader)
    block()
    EndShaderMode()
}

inline fun blendMode(
    mode: BlendMode,
    crossinline block: () -> Unit
) {
    BeginBlendMode(mode.value.toInt())
    block()
    EndBlendMode()
}

//RLAPI void BeginMode3D(Camera3D camera);                          // Begin 3D mode with custom camera (3D)
//RLAPI void EndMode3D(void);                                       // Ends 3D mode and returns to default 2D orthographic mode
//RLAPI void BeginVrStereoMode(VrStereoConfig config);              // Begin stereo rendering (requires VR simulator)
//RLAPI void EndVrStereoMode(void);                                 // End stereo rendering (requires VR simulator)
