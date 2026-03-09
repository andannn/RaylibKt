/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.alloc
import kotlinx.cinterop.cValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.toKString
import kotlinx.cinterop.useContents
import raylib.interop.AudioStream
import raylib.interop.AutomationEvent
import raylib.interop.AutomationEventList
import raylib.interop.BlendMode
import raylib.interop.BoneInfo
import raylib.interop.BoundingBox
import raylib.interop.Camera
import raylib.interop.Camera3D
import raylib.interop.CameraMode
import raylib.interop.CameraProjection
import raylib.interop.ConfigFlags
import raylib.interop.CubemapLayout
import raylib.interop.FilePathList
import raylib.interop.Font
import raylib.interop.FontType
import raylib.interop.GamepadAxis
import raylib.interop.GamepadButton
import raylib.interop.Gesture
import raylib.interop.GlyphInfo
import raylib.interop.Image
import raylib.interop.KeyboardKey
import raylib.interop.Material
import raylib.interop.MaterialMap
import raylib.interop.MaterialMapIndex
import raylib.interop.Mesh
import raylib.interop.Model
import raylib.interop.ModelAnimation
import raylib.interop.MouseButton
import raylib.interop.MouseCursor
import raylib.interop.Music
import raylib.interop.NPatchInfo
import raylib.interop.NPatchLayout
import raylib.interop.PixelFormat
import raylib.interop.Ray
import raylib.interop.RayCollision
import raylib.interop.RenderTexture
import raylib.interop.RenderTexture2D
import raylib.interop.Shader
import raylib.interop.ShaderAttributeDataType
import raylib.interop.ShaderLocationIndex
import raylib.interop.ShaderUniformDataType
import raylib.interop.Sound
import raylib.interop.TextFormat
import raylib.interop.Texture
import raylib.interop.Texture2D
import raylib.interop.TextureCubemap
import raylib.interop.TextureFilter
import raylib.interop.TextureWrap
import raylib.interop.Transform
import raylib.interop.Vector3One
import raylib.interop.Vector3Zero
import raylib.interop.Vector4
import raylib.interop.VrDeviceInfo
import raylib.interop.VrStereoConfig
import raylib.interop.Wave
import raylib.interop.float16
import raylib.interop.float3

typealias Vector3 = raylib.interop.Vector3

object Vector3Factory {
    fun zero(): CValue<Vector3> = Vector3Zero()
    fun one(): CValue<Vector3> = Vector3One()
}
typealias Vector4 = Vector4
typealias Quaternion = Vector4
typealias Vector2 = raylib.interop.Vector2

fun Vector2(x: Float = 0f, y: Float = 0f): CValue<Vector2> = cValue {
    this.x = x
    this.y = y
}
fun NativePlacement.Vector2Alloc(x: Float = 0f, y: Float = 0f) = alloc<Vector2>().apply {
    this.x = x
    this.y = y
}
fun Vector2.set(x: Float = 0f, y: Float = 0f) {
    this.x = x
    this.y = y
}
fun Vector2.set(vec: Vector2) {
    set(vec.readValue())
}
fun Vector2.set(vec: CValue<Vector2>) {
    vec.useContents {
        this@set.set(x, y)
    }
}

typealias Float3 = float3
typealias Float16 = float16
typealias Matrix = raylib.interop.Matrix
fun CValue<Matrix>.format(): String {
    return useContents {
        val row1 = TextFormat("| %7.2f %7.2f %7.2f %7.2f |", m0, m4, m8, m12)?.toKString() ?: ""
        val row2 = TextFormat("| %7.2f %7.2f %7.2f %7.2f |", m1, m5, m9, m13)?.toKString() ?: ""
        val row3 = TextFormat("| %7.2f %7.2f %7.2f %7.2f |", m2, m6, m10, m14)?.toKString() ?: ""
        val row4 = TextFormat("| %7.2f %7.2f %7.2f %7.2f |", m3, m7, m11, m15)?.toKString() ?: ""

        "\n$row1\n$row2\n$row3\n$row4"
    }
}
typealias Color = raylib.interop.Color

fun Color(r: Int, g: Int, b: Int, a: Int = 255): CValue<Color> {
    return cValue {
        this.r = r.toUByte()
        this.g = g.toUByte()
        this.b = b.toUByte()
        this.a = a.toUByte()
    }
}

object Colors {
    val LIGHTGRAY = Color(200, 200, 200, 255)
    val GRAY = Color(130, 130, 130, 255)
    val DARKGRAY = Color(80, 80, 80, 255)
    val YELLOW = Color(253, 249, 0, 255)
    val GOLD = Color(255, 203, 0, 255)
    val ORANGE = Color(255, 161, 0, 255)
    val PINK = Color(255, 109, 194, 255)
    val RED = Color(230, 41, 55, 255)
    val MAROON = Color(190, 33, 55, 255)
    val GREEN = Color(0, 228, 48, 255)
    val LIME = Color(0, 158, 47, 255)
    val DARKGREEN = Color(0, 117, 44, 255)
    val SKYBLUE = Color(102, 191, 255, 255)
    val BLUE = Color(0, 121, 241, 255)
    val DARKBLUE = Color(0, 82, 172, 255)
    val PURPLE = Color(200, 122, 255, 255)
    val VIOLET = Color(135, 60, 190, 255)
    val DARKPURPLE = Color(112, 31, 126, 255)
    val BEIGE = Color(211, 176, 131, 255)
    val BROWN = Color(127, 106, 79, 255)
    val DARKBROWN = Color(76, 63, 47, 255)

    val WHITE = Color(255, 255, 255, 255)
    val BLACK = Color(0, 0, 0, 255)
    val BLANK = Color(0, 0, 0, 0)
    val MAGENTA = Color(255, 0, 255, 255)
    val RAYWHITE = Color(245, 245, 245, 255)
}

typealias Rectangle = raylib.interop.Rectangle

fun NativePlacement.RectangleAlloc(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f
) = alloc<Rectangle>().apply {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}

fun Rectangle.set(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f
)  {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}

fun Rectangle.set(
    rect: Rectangle
) {
    set(rect.readValue())
}

fun Rectangle.set(
    rect: CValue<Rectangle>
) {
    rect.useContents {
        this@set.set(x, y, width, height)
    }
}

fun Rectangle(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f
): CValue<Rectangle> = cValue {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}
typealias Image = Image
typealias Texture = Texture
typealias Texture2D = Texture2D
typealias TextureCubemap = TextureCubemap
typealias RenderTexture = RenderTexture
typealias NPatchInfo = NPatchInfo
typealias GlyphInfo = GlyphInfo
typealias Font = Font
typealias Camera3D = Camera3D
typealias Camera = Camera
typealias Camera2D = raylib.interop.Camera2D

fun Camera2D() = cValue<Camera2D> {}
typealias Mesh = Mesh
typealias Shader = Shader
typealias MaterialMap = MaterialMap
typealias Material = Material
typealias Transform = Transform
typealias BoneInfo = BoneInfo
typealias Model = Model
typealias ModelAnimation = ModelAnimation
typealias Ray = Ray
typealias RayCollision = RayCollision
typealias BoundingBox = BoundingBox
typealias Wave = Wave
typealias AudioStream = AudioStream
typealias Sound = Sound
typealias Music = Music
typealias VrDeviceInfo = VrDeviceInfo
typealias VrStereoConfig = VrStereoConfig
typealias FilePathList = FilePathList
typealias AutomationEvent = AutomationEvent
typealias AutomationEventList = AutomationEventList
typealias ConfigFlags = ConfigFlags
typealias KeyboardKey = KeyboardKey
typealias MouseButton = MouseButton
typealias MouseCursor = MouseCursor
typealias GamepadButton = GamepadButton
typealias GamepadAxis = GamepadAxis
typealias MaterialMapIndex = MaterialMapIndex
typealias ShaderLocationIndex = ShaderLocationIndex
typealias ShaderUniformDataType = ShaderUniformDataType
typealias ShaderAttributeDataType = ShaderAttributeDataType
typealias PixelFormat = PixelFormat
typealias TextureFilter = TextureFilter
typealias TextureWrap = TextureWrap
typealias CubemapLayout = CubemapLayout
typealias FontType = FontType
typealias BlendMode = BlendMode
typealias Gesture = Gesture
typealias CameraMode = CameraMode
typealias CameraProjection = CameraProjection
typealias NPatchLayout = NPatchLayout
typealias RenderTexture2D = RenderTexture2D
