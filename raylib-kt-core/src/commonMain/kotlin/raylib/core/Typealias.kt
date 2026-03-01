package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.alloc
import kotlinx.cinterop.cValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.interop.Vector3One
import raylib.interop.Vector3Zero
import raylib.interop.float16
import raylib.interop.float3

typealias Vector3 = raylib.interop.Vector3

object Vector3Factory {
    fun zero(): CValue<Vector3> = Vector3Zero()
    fun one(): CValue<Vector3> = Vector3One()
}
typealias Vector4 = raylib.interop.Vector4
typealias Quaternion = raylib.interop.Vector4
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
typealias Image = raylib.interop.Image
typealias Texture = raylib.interop.Texture
typealias Texture2D = raylib.interop.Texture2D
typealias TextureCubemap = raylib.interop.TextureCubemap
typealias RenderTexture = raylib.interop.RenderTexture
typealias NPatchInfo = raylib.interop.NPatchInfo
typealias GlyphInfo = raylib.interop.GlyphInfo
typealias Font = raylib.interop.Font
typealias Camera3D = raylib.interop.Camera3D
typealias Camera = raylib.interop.Camera
typealias Camera2D = raylib.interop.Camera2D

fun Camera2D() = cValue<Camera2D> {}
typealias Mesh = raylib.interop.Mesh
typealias Shader = raylib.interop.Shader
typealias MaterialMap = raylib.interop.MaterialMap
typealias Material = raylib.interop.Material
typealias Transform = raylib.interop.Transform
typealias BoneInfo = raylib.interop.BoneInfo
typealias Model = raylib.interop.Model
typealias ModelAnimation = raylib.interop.ModelAnimation
typealias Ray = raylib.interop.Ray
typealias RayCollision = raylib.interop.RayCollision
typealias BoundingBox = raylib.interop.BoundingBox
typealias Wave = raylib.interop.Wave
typealias AudioStream = raylib.interop.AudioStream
typealias Sound = raylib.interop.Sound
typealias Music = raylib.interop.Music
typealias VrDeviceInfo = raylib.interop.VrDeviceInfo
typealias VrStereoConfig = raylib.interop.VrStereoConfig
typealias FilePathList = raylib.interop.FilePathList
typealias AutomationEvent = raylib.interop.AutomationEvent
typealias AutomationEventList = raylib.interop.AutomationEventList
typealias ConfigFlags = raylib.interop.ConfigFlags
typealias KeyboardKey = raylib.interop.KeyboardKey
typealias MouseButton = raylib.interop.MouseButton
typealias MouseCursor = raylib.interop.MouseCursor
typealias GamepadButton = raylib.interop.GamepadButton
typealias GamepadAxis = raylib.interop.GamepadAxis
typealias MaterialMapIndex = raylib.interop.MaterialMapIndex
typealias ShaderLocationIndex = raylib.interop.ShaderLocationIndex
typealias ShaderUniformDataType = raylib.interop.ShaderUniformDataType
typealias ShaderAttributeDataType = raylib.interop.ShaderAttributeDataType
typealias PixelFormat = raylib.interop.PixelFormat
typealias TextureFilter = raylib.interop.TextureFilter
typealias TextureWrap = raylib.interop.TextureWrap
typealias CubemapLayout = raylib.interop.CubemapLayout
typealias FontType = raylib.interop.FontType
typealias BlendMode = raylib.interop.BlendMode
typealias Gesture = raylib.interop.Gesture
typealias CameraMode = raylib.interop.CameraMode
typealias CameraProjection = raylib.interop.CameraProjection
typealias NPatchLayout = raylib.interop.NPatchLayout
typealias RenderTexture2D = raylib.interop.RenderTexture2D
