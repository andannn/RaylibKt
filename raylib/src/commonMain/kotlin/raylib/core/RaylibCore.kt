@file:Suppress("ktlint:standard:no-wildcard-imports")

package raylib.core

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.cValue
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKString
import platform.posix.va_list
import raylib.interop.AudioCallback
import raylib.math.Matrix
import raylib.math.Vector2
import raylib.math.Vector3
import raylib.math.Vector4

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


fun interface TraceLogCallback {
    fun invoke(logLevel: Int, message: String?, args: va_list?)
}

object RlWindow {
    fun init(width: Int, height: Int, title: String?) =
        raylib.interop.InitWindow(width, height, title)

    fun close() = raylib.interop.CloseWindow()
    fun shouldClose(): Boolean = raylib.interop.WindowShouldClose()
    fun isReady(): Boolean = raylib.interop.IsWindowReady()
    fun isFullscreen(): Boolean = raylib.interop.IsWindowFullscreen()
    fun isHidden(): Boolean = raylib.interop.IsWindowHidden()
    fun isMinimized(): Boolean = raylib.interop.IsWindowMinimized()
    fun isMaximized(): Boolean = raylib.interop.IsWindowMaximized()
    fun isFocused(): Boolean = raylib.interop.IsWindowFocused()
    fun isResized(): Boolean = raylib.interop.IsWindowResized()
    fun isState(flag: UInt): Boolean = raylib.interop.IsWindowState(flag)
    fun setState(flags: UInt) = raylib.interop.SetWindowState(flags)
    fun clearState(flags: UInt) = raylib.interop.ClearWindowState(flags)
    fun toggleFullscreen() = raylib.interop.ToggleFullscreen()
    fun toggleBorderlessWindowed() = raylib.interop.ToggleBorderlessWindowed()
    fun maximize() = raylib.interop.MaximizeWindow()
    fun minimize() = raylib.interop.MinimizeWindow()
    fun restore() = raylib.interop.RestoreWindow()
    fun setIcon(image: CValue<Image>) = raylib.interop.SetWindowIcon(image)
    fun setIcons(images: CPointer<Image>?, count: Int) =
        raylib.interop.SetWindowIcons(images, count)

    fun setTitle(title: String?) = raylib.interop.SetWindowTitle(title)
    fun setPosition(x: Int, y: Int) = raylib.interop.SetWindowPosition(x, y)
    fun setMonitor(monitor: Int) = raylib.interop.SetWindowMonitor(monitor)
    fun setMinSize(width: Int, height: Int) = raylib.interop.SetWindowMinSize(width, height)
    fun setMaxSize(width: Int, height: Int) = raylib.interop.SetWindowMaxSize(width, height)
    fun setSize(width: Int, height: Int) = raylib.interop.SetWindowSize(width, height)
    fun setOpacity(opacity: Float) = raylib.interop.SetWindowOpacity(opacity)
    fun focus() = raylib.interop.SetWindowFocused()
    fun getHandle(): CPointer<*>? = raylib.interop.GetWindowHandle()
    fun screenWidth(): Int = raylib.interop.GetScreenWidth()
    fun screenHeight(): Int = raylib.interop.GetScreenHeight()
    fun renderWidth(): Int = raylib.interop.GetRenderWidth()
    fun renderHeight(): Int = raylib.interop.GetRenderHeight()
    fun monitorCount(): Int = raylib.interop.GetMonitorCount()
    fun currentMonitor(): Int = raylib.interop.GetCurrentMonitor()
    fun monitorPosition(monitor: Int): CValue<Vector2> = raylib.interop.GetMonitorPosition(monitor)
    fun monitorWidth(monitor: Int): Int = raylib.interop.GetMonitorWidth(monitor)
    fun monitorHeight(monitor: Int): Int = raylib.interop.GetMonitorHeight(monitor)
    fun monitorPhysicalWidth(monitor: Int): Int = raylib.interop.GetMonitorPhysicalWidth(monitor)
    fun monitorPhysicalHeight(monitor: Int): Int = raylib.interop.GetMonitorPhysicalHeight(monitor)
    fun monitorRefreshRate(monitor: Int): Int = raylib.interop.GetMonitorRefreshRate(monitor)
    fun position(): CValue<Vector2> = raylib.interop.GetWindowPosition()
    fun scaleDpi(): CValue<Vector2> = raylib.interop.GetWindowScaleDPI()
    fun monitorName(monitor: Int): String? = raylib.interop.GetMonitorName(monitor)?.toKString()
    fun setClipboardText(text: String?) = raylib.interop.SetClipboardText(text)
    fun clipboardText(): String? = raylib.interop.GetClipboardText()?.toKString()
    fun enableEventWaiting() = raylib.interop.EnableEventWaiting()
    fun disableEventWaiting() = raylib.interop.DisableEventWaiting()
}

object RlCursor {
    fun show() = raylib.interop.ShowCursor()
    fun hide() = raylib.interop.HideCursor()
    fun isHidden(): Boolean = raylib.interop.IsCursorHidden()
    fun enable() = raylib.interop.EnableCursor()
    fun disable() = raylib.interop.DisableCursor()
    fun isOnScreen(): Boolean = raylib.interop.IsCursorOnScreen()
}

object DrawScope {
    fun clearBackground(color: CValue<Color>): Unit = raylib.interop.ClearBackground(color)
    fun begin() = raylib.interop.BeginDrawing()
    fun end() = raylib.interop.EndDrawing()
    fun beginMode2D(camera: CValue<Camera2D>) = raylib.interop.BeginMode2D(camera)
    fun endMode2D() = raylib.interop.EndMode2D()
    fun beginMode3D(camera: CValue<Camera3D>) = raylib.interop.BeginMode3D(camera)
    fun endMode3D() = raylib.interop.EndMode3D()
    fun beginTextureMode(target: CValue<RenderTexture2D>) = raylib.interop.BeginTextureMode(target)
    fun endTextureMode() = raylib.interop.EndTextureMode()
    fun beginShaderMode(shader: CValue<Shader>) = raylib.interop.BeginShaderMode(shader)
    fun endShaderMode() = raylib.interop.EndShaderMode()
    fun beginBlendMode(mode: Int) = raylib.interop.BeginBlendMode(mode)
    fun endBlendMode() = raylib.interop.EndBlendMode()
    fun beginScissorMode(x: Int, y: Int, width: Int, height: Int) =
        raylib.interop.BeginScissorMode(x, y, width, height)

    fun endScissorMode() = raylib.interop.EndScissorMode()
    fun beginVrStereoMode(config: CValue<VrStereoConfig>) = raylib.interop.BeginVrStereoMode(config)
    fun endVrStereoMode() = raylib.interop.EndVrStereoMode()
}

object RlVr {
    fun loadConfig(device: CValue<VrDeviceInfo>): CValue<VrStereoConfig> =
        raylib.interop.LoadVrStereoConfig(device)

    fun unloadConfig(config: CValue<VrStereoConfig>) = raylib.interop.UnloadVrStereoConfig(config)
}

object RlShader {
    fun loadShader(vsFileName: String?, fsFileName: String?): CValue<Shader> =
        raylib.interop.LoadShader(vsFileName, fsFileName)

    fun loadShaderFromMemory(vsCode: String?, fsCode: String?): CValue<Shader> =
        raylib.interop.LoadShaderFromMemory(vsCode, fsCode)

//    fun isShaderReady(shader: CValue<Shader>): Boolean = raylib.interop.IsShaderReady(shader)
    fun getShaderLocation(shader: CValue<Shader>, uniformName: String?): Int =
        raylib.interop.GetShaderLocation(shader, uniformName)

    fun getShaderLocationAttrib(shader: CValue<Shader>, attribName: String?): Int =
        raylib.interop.GetShaderLocationAttrib(shader, attribName)

    fun setShaderValue(
        shader: CValue<Shader>,
        locIndex: Int,
        value: CPointer<*>?,
        uniformType: Int
    ) = raylib.interop.SetShaderValue(shader, locIndex, value, uniformType)

    fun setShaderValueV(
        shader: CValue<Shader>,
        locIndex: Int,
        value: CPointer<*>?,
        uniformType: Int,
        count: Int
    ) = raylib.interop.SetShaderValueV(shader, locIndex, value, uniformType, count)

    fun setShaderValueMatrix(shader: CValue<Shader>, locIndex: Int, mat: CValue<Matrix>) =
        raylib.interop.SetShaderValueMatrix(shader, locIndex, mat)

    fun setShaderValueTexture(shader: CValue<Shader>, locIndex: Int, texture: CValue<Texture2D>) =
        raylib.interop.SetShaderValueTexture(shader, locIndex, texture)

    fun unloadShader(shader: CValue<Shader>) = raylib.interop.UnloadShader(shader)
}

object RlScreenSpace {
//    fun getMouseRay(mousePosition: CValue<Vector2>, camera: CValue<Camera>): CValue<Ray> =
//        raylib.interop.GetMouseRay(mousePosition, camera)

    fun getCameraMatrix(camera: CValue<Camera>): CValue<Matrix> =
        raylib.interop.GetCameraMatrix(camera)

    fun getCameraMatrix2D(camera: CValue<Camera2D>): CValue<Matrix> =
        raylib.interop.GetCameraMatrix2D(camera)

    fun getWorldToScreen(position: CValue<Vector3>, camera: CValue<Camera>): CValue<Vector2> =
        raylib.interop.GetWorldToScreen(position, camera)

    fun getScreenToWorld2D(position: CValue<Vector2>, camera: CValue<Camera2D>): CValue<Vector2> =
        raylib.interop.GetScreenToWorld2D(position, camera)

    fun getWorldToScreenEx(
        position: CValue<Vector3>,
        camera: CValue<Camera>,
        width: Int,
        height: Int
    ): CValue<Vector2> = raylib.interop.GetWorldToScreenEx(position, camera, width, height)

    fun getWorldToScreen2D(position: CValue<Vector2>, camera: CValue<Camera2D>): CValue<Vector2> =
        raylib.interop.GetWorldToScreen2D(position, camera)
}

object RlTiming {
    fun setTargetFPS(fps: Int) = raylib.interop.SetTargetFPS(fps)
    fun getFrameTime(): Float = raylib.interop.GetFrameTime()
    fun getTime(): Double = raylib.interop.GetTime()
    fun getFPS(): Int = raylib.interop.GetFPS()
}

object RlFrameControl {
    fun swapScreenBuffer() = raylib.interop.SwapScreenBuffer()
    fun pollInputEvents() = raylib.interop.PollInputEvents()
    fun waitTime(seconds: Double) = raylib.interop.WaitTime(seconds)
}

object RlRandomValues {
    fun setRandomSeed(seed: UInt) = raylib.interop.SetRandomSeed(seed)
    fun getRandomValue(min: Int, max: Int): Int = raylib.interop.GetRandomValue(min, max)
    fun loadRandomSequence(count: UInt, min: Int, max: Int): CPointer<IntVar>? =
        raylib.interop.LoadRandomSequence(count, min, max)

    fun unloadRandomSequence(sequence: CPointer<IntVar>?) =
        raylib.interop.UnloadRandomSequence(sequence)
}

object RlMisc {
    fun takeScreenshot(fileName: String?) = raylib.interop.TakeScreenshot(fileName)
    fun setConfigFlags(flags: UInt) = raylib.interop.SetConfigFlags(flags)
    fun openURL(url: String?) = raylib.interop.OpenURL(url)
}


object RlLog {
    fun traceLog(logLevel: Int, text: String?) = raylib.interop.TraceLog(logLevel, text)
    fun setLevel(logLevel: Int) = raylib.interop.SetTraceLogLevel(logLevel)
}

object RlMem {
    fun alloc(size: UInt): CPointer<*>? = raylib.interop.MemAlloc(size)
    fun realloc(ptr: CPointer<*>?, size: UInt): CPointer<*>? = raylib.interop.MemRealloc(ptr, size)
    fun free(ptr: CPointer<*>?) = raylib.interop.MemFree(ptr)
}

object RlCallbacks {
//    fun setTraceLogCallback(callback: TraceLogCallback) = raylib.interop.SetTraceLogCallback(
//        callback = staticCFunction { logLevel, message, args ->
//            callback.invoke(logLevel, message?.toKString(), args)
//        }
//    )
//
//    fun setTraceLog(callback: CValue<TraceLogCallback>) =
//        raylib.interop.SetTraceLogCallback(callback)
//
//    fun setLoadFileData(callback: CValue<LoadFileDataCallback>) =
//        raylib.interop.SetLoadFileDataCallback(callback)
//
//    fun setSaveFileData(callback: CValue<SaveFileDataCallback>) =
//        raylib.interop.SetSaveFileDataCallback(callback)
//
//    fun setLoadFileText(callback: CValue<LoadFileTextCallback>) =
//        raylib.interop.SetLoadFileTextCallback(callback)
//
//    fun setSaveFileText(callback: CValue<SaveFileTextCallback>) =
//        raylib.interop.SetSaveFileTextCallback(callback)
}

object RlFiles {
    fun loadData(fileName: String?, dataSize: CPointer<IntVar>?): CPointer<UByteVar>? =
        raylib.interop.LoadFileData(fileName, dataSize)

    fun unloadData(data: CPointer<UByteVar>?) = raylib.interop.UnloadFileData(data)
    fun saveData(fileName: String?, data: CPointer<*>?, dataSize: Int): Boolean =
        raylib.interop.SaveFileData(fileName, data, dataSize)

    fun exportDataAsCode(data: CPointer<UByteVar>?, dataSize: Int, fileName: String?): Boolean =
        raylib.interop.ExportDataAsCode(data, dataSize, fileName)

    fun loadText(fileName: String): String? = raylib.interop.LoadFileText(fileName)?.toKString()
    fun unloadText(text: String) = raylib.interop.UnloadFileText(text.cstr)
//    fun saveText(fileName: String, text: String): Boolean =
//        raylib.interop.SaveFileText(fileName, text)
}

object RlFs {
    fun fileExists(fileName: String?): Boolean = raylib.interop.FileExists(fileName)
    fun directoryExists(dirPath: String?): Boolean = raylib.interop.DirectoryExists(dirPath)
    fun isFileExtension(fileName: String?, ext: String?): Boolean =
        raylib.interop.IsFileExtension(fileName, ext)

    fun getFileLength(fileName: String?): Int = raylib.interop.GetFileLength(fileName)
    fun getFileExtension(fileName: String?): String? =
        raylib.interop.GetFileExtension(fileName)?.toKString()

    fun getFileName(filePath: String?): String? = raylib.interop.GetFileName(filePath)?.toKString()
    fun getFileNameWithoutExt(filePath: String?): String? =
        raylib.interop.GetFileNameWithoutExt(filePath)?.toKString()

    fun getDirectoryPath(filePath: String?): String? =
        raylib.interop.GetDirectoryPath(filePath)?.toKString()

    fun getPrevDirectoryPath(dirPath: String?): String? =
        raylib.interop.GetPrevDirectoryPath(dirPath)?.toKString()

    fun getWorkingDirectory(): String? = raylib.interop.GetWorkingDirectory()?.toKString()
    fun getApplicationDirectory(): String? = raylib.interop.GetApplicationDirectory()?.toKString()
    fun changeDirectory(dir: String?): Boolean = raylib.interop.ChangeDirectory(dir)
    fun isPathFile(path: String?): Boolean = raylib.interop.IsPathFile(path)
    fun loadDirectoryFiles(dirPath: String?): CValue<FilePathList> =
        raylib.interop.LoadDirectoryFiles(dirPath)

    fun loadDirectoryFilesEx(
        basePath: String?,
        filter: String?,
        scanSubdirs: Boolean
    ): CValue<FilePathList> = raylib.interop.LoadDirectoryFilesEx(basePath, filter, scanSubdirs)

    fun unloadDirectoryFiles(files: CValue<FilePathList>) =
        raylib.interop.UnloadDirectoryFiles(files)

    fun isFileDropped(): Boolean = raylib.interop.IsFileDropped()
    fun loadDroppedFiles(): CValue<FilePathList> = raylib.interop.LoadDroppedFiles()
    fun unloadDroppedFiles(files: CValue<FilePathList>) = raylib.interop.UnloadDroppedFiles(files)
//    fun getFileModTime(fileName: String?): Long = raylib.interop.GetFileModTime(fileName)
}

object RlCompression {
    fun compressData(
        data: CPointer<UByteVar>?,
        dataSize: Int,
        compDataSize: CPointer<IntVar>?
    ): CPointer<UByteVar>? = raylib.interop.CompressData(data, dataSize, compDataSize)

    fun decompressData(
        compData: CPointer<UByteVar>?,
        compDataSize: Int,
        dataSize: CPointer<IntVar>?
    ): CPointer<UByteVar>? = raylib.interop.DecompressData(compData, compDataSize, dataSize)

    fun encodeDataBase64(
        data: CPointer<UByteVar>?,
        dataSize: Int,
        outputSize: CPointer<IntVar>?
    ): String? = raylib.interop.EncodeDataBase64(data, dataSize, outputSize)?.toKString()

//    fun decodeDataBase64(
//        data: String?,
//        outputSize: CPointer<IntVar>?
//    ): CPointer<UByteVar>? = raylib.interop.DecodeDataBase64(data, outputSize)
}

object RlAutomation {
    fun loadEventList(fileName: String?): CValue<AutomationEventList> =
        raylib.interop.LoadAutomationEventList(fileName)

//    fun unloadEventList(list: CPointer<AutomationEventList>?) =
//        raylib.interop.UnloadAutomationEventList(list)

    fun exportEventList(list: CValue<AutomationEventList>, fileName: String?): Boolean =
        raylib.interop.ExportAutomationEventList(list, fileName)

    fun setEventList(list: CPointer<AutomationEventList>?) =
        raylib.interop.SetAutomationEventList(list)

    fun setBaseFrame(frame: Int) = raylib.interop.SetAutomationEventBaseFrame(frame)
    fun startRecording() = raylib.interop.StartAutomationEventRecording()
    fun stopRecording() = raylib.interop.StopAutomationEventRecording()
    fun playEvent(event: CValue<AutomationEvent>) = raylib.interop.PlayAutomationEvent(event)
}

object RlKeyboard {
    fun isKeyPressed(key: Int): Boolean = raylib.interop.IsKeyPressed(key)
    fun isKeyPressedRepeat(key: Int): Boolean = raylib.interop.IsKeyPressedRepeat(key)
    fun isKeyDown(key: Int): Boolean = raylib.interop.IsKeyDown(key)
    fun isKeyReleased(key: Int): Boolean = raylib.interop.IsKeyReleased(key)
    fun isKeyUp(key: Int): Boolean = raylib.interop.IsKeyUp(key)
    fun getKeyPressed(): Int = raylib.interop.GetKeyPressed()
    fun getCharPressed(): Int = raylib.interop.GetCharPressed()
    fun setExitKey(key: Int) = raylib.interop.SetExitKey(key)
}

object RlGamepad {
    fun isAvailable(gamepad: Int): Boolean = raylib.interop.IsGamepadAvailable(gamepad)
    fun name(gamepad: Int): String? = raylib.interop.GetGamepadName(gamepad)?.toKString()
    fun isButtonPressed(gamepad: Int, button: Int): Boolean =
        raylib.interop.IsGamepadButtonPressed(gamepad, button)

    fun isButtonDown(gamepad: Int, button: Int): Boolean =
        raylib.interop.IsGamepadButtonDown(gamepad, button)

    fun isButtonReleased(gamepad: Int, button: Int): Boolean =
        raylib.interop.IsGamepadButtonReleased(gamepad, button)

    fun isButtonUp(gamepad: Int, button: Int): Boolean =
        raylib.interop.IsGamepadButtonUp(gamepad, button)

    fun getButtonPressed(): Int = raylib.interop.GetGamepadButtonPressed()
    fun axisCount(gamepad: Int): Int = raylib.interop.GetGamepadAxisCount(gamepad)
    fun axisMovement(gamepad: Int, axis: Int): Float =
        raylib.interop.GetGamepadAxisMovement(gamepad, axis)

    fun setMappings(mappings: String?): Int = raylib.interop.SetGamepadMappings(mappings)
}

object RlMouse {
    fun isButtonPressed(button: Int): Boolean = raylib.interop.IsMouseButtonPressed(button)
    fun isButtonDown(button: Int): Boolean = raylib.interop.IsMouseButtonDown(button)
    fun isButtonReleased(button: Int): Boolean = raylib.interop.IsMouseButtonReleased(button)
    fun isButtonUp(button: Int): Boolean = raylib.interop.IsMouseButtonUp(button)
    fun x(): Int = raylib.interop.GetMouseX()
    fun y(): Int = raylib.interop.GetMouseY()
    fun position(): CValue<Vector2> = raylib.interop.GetMousePosition()
    fun delta(): CValue<Vector2> = raylib.interop.GetMouseDelta()
    fun setPosition(x: Int, y: Int) = raylib.interop.SetMousePosition(x, y)
    fun setOffset(offsetX: Int, offsetY: Int) = raylib.interop.SetMouseOffset(offsetX, offsetY)
    fun setScale(scaleX: Float, scaleY: Float) = raylib.interop.SetMouseScale(scaleX, scaleY)
    fun wheelMove(): Float = raylib.interop.GetMouseWheelMove()
    fun wheelMoveV(): CValue<Vector2> = raylib.interop.GetMouseWheelMoveV()
    fun setCursor(cursor: Int) = raylib.interop.SetMouseCursor(cursor)
}

object RlTouch {
    fun x(): Int = raylib.interop.GetTouchX()
    fun y(): Int = raylib.interop.GetTouchY()
    fun position(index: Int): CValue<Vector2> = raylib.interop.GetTouchPosition(index)
    fun pointId(index: Int): Int = raylib.interop.GetTouchPointId(index)
    fun pointCount(): Int = raylib.interop.GetTouchPointCount()
}

object RlGestures {
    fun setEnabled(flags: UInt) = raylib.interop.SetGesturesEnabled(flags)
    fun isDetected(gesture: UInt): Boolean = raylib.interop.IsGestureDetected(gesture)
    fun detected(): Int = raylib.interop.GetGestureDetected()
    fun holdDuration(): Float = raylib.interop.GetGestureHoldDuration()
    fun dragVector(): CValue<Vector2> = raylib.interop.GetGestureDragVector()
    fun dragAngle(): Float = raylib.interop.GetGestureDragAngle()
    fun pinchVector(): CValue<Vector2> = raylib.interop.GetGesturePinchVector()
    fun pinchAngle(): Float = raylib.interop.GetGesturePinchAngle()
}

object RlCamera {
    fun update(camera: CPointer<Camera>?, mode: Int) = raylib.interop.UpdateCamera(camera, mode)
    fun updatePro(
        camera: CPointer<Camera>?,
        movement: CValue<Vector3>,
        rotation: CValue<Vector3>,
        zoom: Float
    ) = raylib.interop.UpdateCameraPro(camera, movement, rotation, zoom)
}

object RlTexture {
    fun setTexture(texture: CValue<Texture2D>, source: CValue<Rectangle>) =
        raylib.interop.SetShapesTexture(texture, source)
}

object RlBasicDraw {
    fun drawPixel(x: Int, y: Int, color: CValue<Color>) = raylib.interop.DrawPixel(x, y, color)
    fun drawPixel(position: CValue<Vector2>, color: CValue<Color>) =
        raylib.interop.DrawPixelV(position, color)

    fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>) =
        raylib.interop.DrawLine(x1, y1, x2, y2, color)

    fun drawLine(start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>) =
        raylib.interop.DrawLineV(start, end, color)

    fun drawLineEx(
        start: CValue<Vector2>,
        end: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawLineEx(start, end, thick, color)

    fun drawLineStrip(points: CPointer<Vector2>?, pointCount: Int, color: CValue<Color>) =
        raylib.interop.DrawLineStrip(points, pointCount, color)

    fun drawLineBezier(
        start: CValue<Vector2>,
        end: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawLineBezier(start, end, thick, color)

    fun drawCircle(x: Int, y: Int, radius: Float, color: CValue<Color>) =
        raylib.interop.DrawCircle(x, y, radius, color)

    fun drawCircleSector(
        center: CValue<Vector2>,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCircleSector(center, radius, startAngle, endAngle, segments, color)

    fun drawCircleSectorLines(
        center: CValue<Vector2>,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCircleSectorLines(center, radius, startAngle, endAngle, segments, color)

    fun drawCircleGradient(
        x: Int,
        y: Int,
        radius: Float,
        color1: CValue<Color>,
        color2: CValue<Color>
    ) = raylib.interop.DrawCircleGradient(x, y, radius, color1, color2)

    fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>) =
        raylib.interop.DrawCircleV(center, radius, color)

    fun drawCircleLines(x: Int, y: Int, radius: Float, color: CValue<Color>) =
        raylib.interop.DrawCircleLines(x, y, radius, color)

    fun drawCircleLines(center: CValue<Vector2>, radius: Float, color: CValue<Color>) =
        raylib.interop.DrawCircleLinesV(center, radius, color)

    fun drawEllipse(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>) =
        raylib.interop.DrawEllipse(x, y, radiusH, radiusV, color)

    fun drawEllipseLines(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>) =
        raylib.interop.DrawEllipseLines(x, y, radiusH, radiusV, color)

    fun drawRing(
        center: CValue<Vector2>,
        innerRadius: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawRing(
        center,
        innerRadius,
        outerRadius,
        startAngle,
        endAngle,
        segments,
        color
    )

    fun drawRingLines(
        center: CValue<Vector2>,
        innerRadius: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawRingLines(
        center,
        innerRadius,
        outerRadius,
        startAngle,
        endAngle,
        segments,
        color
    )

    fun drawRectangle(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>) =
        raylib.interop.DrawRectangle(x, y, width, height, color)

    fun drawRectangle(position: CValue<Vector2>, size: CValue<Vector2>, color: CValue<Color>) =
        raylib.interop.DrawRectangleV(position, size, color)

    fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>) =
        raylib.interop.DrawRectangleRec(rec, color)

    fun drawRectanglePro(
        rec: CValue<Rectangle>,
        origin: CValue<Vector2>,
        rotation: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawRectanglePro(rec, origin, rotation, color)

    fun drawRectangleGradientV(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color1: CValue<Color>,
        color2: CValue<Color>
    ) = raylib.interop.DrawRectangleGradientV(x, y, width, height, color1, color2)

    fun drawRectangleGradientH(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color1: CValue<Color>,
        color2: CValue<Color>
    ) = raylib.interop.DrawRectangleGradientH(x, y, width, height, color1, color2)

    fun drawRectangleGradientEx(
        rec: CValue<Rectangle>,
        c1: CValue<Color>,
        c2: CValue<Color>,
        c3: CValue<Color>,
        c4: CValue<Color>
    ) = raylib.interop.DrawRectangleGradientEx(rec, c1, c2, c3, c4)

    fun drawRectangleLines(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>) =
        raylib.interop.DrawRectangleLines(x, y, width, height, color)

    fun drawRectangleLinesEx(rec: CValue<Rectangle>, lineThick: Float, color: CValue<Color>) =
        raylib.interop.DrawRectangleLinesEx(rec, lineThick, color)

    fun drawRectangleRounded(
        rec: CValue<Rectangle>,
        roundness: Float,
        segments: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawRectangleRounded(rec, roundness, segments, color)

//    fun drawRectangleRoundedLines(
//        rec: CValue<Rectangle>,
//        roundness: Float,
//        segments: Int,
//        lineThick: Float,
//        color: CValue<Color>
//    ) = raylib.interop.DrawRectangleRoundedLines(rec, roundness, segments, lineThick, color)

    fun drawTriangle(
        v1: CValue<Vector2>,
        v2: CValue<Vector2>,
        v3: CValue<Vector2>,
        color: CValue<Color>
    ) = raylib.interop.DrawTriangle(v1, v2, v3, color)

    fun drawTriangleLines(
        v1: CValue<Vector2>,
        v2: CValue<Vector2>,
        v3: CValue<Vector2>,
        color: CValue<Color>
    ) = raylib.interop.DrawTriangleLines(v1, v2, v3, color)

    fun drawTriangleFan(points: CPointer<Vector2>?, pointCount: Int, color: CValue<Color>) =
        raylib.interop.DrawTriangleFan(points, pointCount, color)

    fun drawTriangleStrip(points: CPointer<Vector2>?, pointCount: Int, color: CValue<Color>) =
        raylib.interop.DrawTriangleStrip(points, pointCount, color)

    fun drawPoly(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawPoly(center, sides, radius, rotation, color)

    fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawPolyLines(center, sides, radius, rotation, color)

    fun drawPolyLinesEx(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        lineThick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawPolyLinesEx(center, sides, radius, rotation, lineThick, color)
}

object RlSplines {
    fun drawLinear(
        points: CPointer<Vector2>?,
        pointCount: Int,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineLinear(points, pointCount, thick, color)

    fun drawBasis(points: CPointer<Vector2>?, pointCount: Int, thick: Float, color: CValue<Color>) =
        raylib.interop.DrawSplineBasis(points, pointCount, thick, color)

    fun drawCatmullRom(
        points: CPointer<Vector2>?,
        pointCount: Int,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineCatmullRom(points, pointCount, thick, color)

    fun drawBezierQuadratic(
        points: CPointer<Vector2>?,
        pointCount: Int,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineBezierQuadratic(points, pointCount, thick, color)

    fun drawBezierCubic(
        points: CPointer<Vector2>?,
        pointCount: Int,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineBezierCubic(points, pointCount, thick, color)

    fun drawSegmentLinear(
        p1: CValue<Vector2>,
        p2: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineSegmentLinear(p1, p2, thick, color)

    fun drawSegmentBasis(
        p1: CValue<Vector2>,
        p2: CValue<Vector2>,
        p3: CValue<Vector2>,
        p4: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineSegmentBasis(p1, p2, p3, p4, thick, color)

    fun drawSegmentCatmullRom(
        p1: CValue<Vector2>,
        p2: CValue<Vector2>,
        p3: CValue<Vector2>,
        p4: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineSegmentCatmullRom(p1, p2, p3, p4, thick, color)

    fun drawSegmentBezierQuadratic(
        p1: CValue<Vector2>,
        c2: CValue<Vector2>,
        p3: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineSegmentBezierQuadratic(p1, c2, p3, thick, color)

    fun drawSegmentBezierCubic(
        p1: CValue<Vector2>,
        c2: CValue<Vector2>,
        c3: CValue<Vector2>,
        p4: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawSplineSegmentBezierCubic(p1, c2, c3, p4, thick, color)
}

object RlSplinePoints {
    fun linear(start: CValue<Vector2>, end: CValue<Vector2>, t: Float): CValue<Vector2> =
        raylib.interop.GetSplinePointLinear(start, end, t)

    fun basis(
        p1: CValue<Vector2>,
        p2: CValue<Vector2>,
        p3: CValue<Vector2>,
        p4: CValue<Vector2>,
        t: Float
    ): CValue<Vector2> = raylib.interop.GetSplinePointBasis(p1, p2, p3, p4, t)

    fun catmullRom(
        p1: CValue<Vector2>,
        p2: CValue<Vector2>,
        p3: CValue<Vector2>,
        p4: CValue<Vector2>,
        t: Float
    ): CValue<Vector2> = raylib.interop.GetSplinePointCatmullRom(p1, p2, p3, p4, t)

    fun bezierQuad(
        p1: CValue<Vector2>,
        c2: CValue<Vector2>,
        p3: CValue<Vector2>,
        t: Float
    ): CValue<Vector2> = raylib.interop.GetSplinePointBezierQuad(p1, c2, p3, t)

    fun bezierCubic(
        p1: CValue<Vector2>,
        c2: CValue<Vector2>,
        c3: CValue<Vector2>,
        p4: CValue<Vector2>,
        t: Float
    ): CValue<Vector2> = raylib.interop.GetSplinePointBezierCubic(p1, c2, c3, p4, t)
}

object RlCollision {
    fun recs(rec1: CValue<Rectangle>, rec2: CValue<Rectangle>): Boolean =
        raylib.interop.CheckCollisionRecs(rec1, rec2)

    fun circles(
        center1: CValue<Vector2>,
        radius1: Float,
        center2: CValue<Vector2>,
        radius2: Float
    ): Boolean = raylib.interop.CheckCollisionCircles(center1, radius1, center2, radius2)

    fun circleRec(center: CValue<Vector2>, radius: Float, rec: CValue<Rectangle>): Boolean =
        raylib.interop.CheckCollisionCircleRec(center, radius, rec)

    fun pointRec(point: CValue<Vector2>, rec: CValue<Rectangle>): Boolean =
        raylib.interop.CheckCollisionPointRec(point, rec)

    fun pointCircle(point: CValue<Vector2>, center: CValue<Vector2>, radius: Float): Boolean =
        raylib.interop.CheckCollisionPointCircle(point, center, radius)

    fun pointTriangle(
        point: CValue<Vector2>,
        p1: CValue<Vector2>,
        p2: CValue<Vector2>,
        p3: CValue<Vector2>
    ): Boolean = raylib.interop.CheckCollisionPointTriangle(point, p1, p2, p3)

    fun pointPoly(point: CValue<Vector2>, points: CPointer<Vector2>?, pointCount: Int): Boolean =
        raylib.interop.CheckCollisionPointPoly(point, points, pointCount)

    fun lines(
        start1: CValue<Vector2>,
        end1: CValue<Vector2>,
        start2: CValue<Vector2>,
        end2: CValue<Vector2>,
        collisionPoint: CPointer<Vector2>?
    ): Boolean = raylib.interop.CheckCollisionLines(start1, end1, start2, end2, collisionPoint)

    fun pointLine(
        point: CValue<Vector2>,
        p1: CValue<Vector2>,
        p2: CValue<Vector2>,
        threshold: Int
    ): Boolean = raylib.interop.CheckCollisionPointLine(point, p1, p2, threshold)

    fun rec(rec1: CValue<Rectangle>, rec2: CValue<Rectangle>): CValue<Rectangle> =
        raylib.interop.GetCollisionRec(rec1, rec2)
}


object RlImages {
    fun load(fileName: String?): CValue<Image> = raylib.interop.LoadImage(fileName)
    fun loadRaw(
        fileName: String?,
        width: Int,
        height: Int,
        format: Int,
        headerSize: Int
    ): CValue<Image> = raylib.interop.LoadImageRaw(fileName, width, height, format, headerSize)

//    fun loadSvg(fileNameOrString: String?, width: Int, height: Int): CValue<Image> =
//        raylib.interop.LoadImageSvg(fileNameOrString, width, height)

    fun loadAnim(fileName: String?, frames: CPointer<IntVar>?): CValue<Image> =
        raylib.interop.LoadImageAnim(fileName, frames)

    fun loadFromMemory(fileType: String?, data: CPointer<UByteVar>?, dataSize: Int): CValue<Image> =
        raylib.interop.LoadImageFromMemory(fileType, data, dataSize)

    fun loadFromTexture(texture: CValue<Texture2D>): CValue<Image> =
        raylib.interop.LoadImageFromTexture(texture)

    fun loadFromScreen(): CValue<Image> = raylib.interop.LoadImageFromScreen()
//    fun isReady(image: CValue<Image>): Boolean = raylib.interop.IsImageReady(image)
    fun unload(image: CValue<Image>) = raylib.interop.UnloadImage(image)
    fun export(image: CValue<Image>, fileName: String?): Boolean =
        raylib.interop.ExportImage(image, fileName)

    fun exportToMemory(
        image: CValue<Image>,
        fileType: String?,
        fileSize: CPointer<IntVar>?
    ): CPointer<UByteVar>? = raylib.interop.ExportImageToMemory(image, fileType, fileSize)

    fun exportAsCode(image: CValue<Image>, fileName: String?): Boolean =
        raylib.interop.ExportImageAsCode(image, fileName)
}

object RlImageGen {
    fun color(width: Int, height: Int, color: CValue<Color>): CValue<Image> =
        raylib.interop.GenImageColor(width, height, color)

    fun gradientLinear(
        width: Int,
        height: Int,
        direction: Int,
        start: CValue<Color>,
        end: CValue<Color>
    ): CValue<Image> = raylib.interop.GenImageGradientLinear(width, height, direction, start, end)

    fun gradientRadial(
        width: Int,
        height: Int,
        density: Float,
        inner: CValue<Color>,
        outer: CValue<Color>
    ): CValue<Image> = raylib.interop.GenImageGradientRadial(width, height, density, inner, outer)

    fun gradientSquare(
        width: Int,
        height: Int,
        density: Float,
        inner: CValue<Color>,
        outer: CValue<Color>
    ): CValue<Image> = raylib.interop.GenImageGradientSquare(width, height, density, inner, outer)

    fun checked(
        width: Int,
        height: Int,
        checksX: Int,
        checksY: Int,
        col1: CValue<Color>,
        col2: CValue<Color>
    ): CValue<Image> = raylib.interop.GenImageChecked(width, height, checksX, checksY, col1, col2)

    fun whiteNoise(width: Int, height: Int, factor: Float): CValue<Image> =
        raylib.interop.GenImageWhiteNoise(width, height, factor)

    fun perlinNoise(
        width: Int,
        height: Int,
        offsetX: Int,
        offsetY: Int,
        scale: Float
    ): CValue<Image> = raylib.interop.GenImagePerlinNoise(width, height, offsetX, offsetY, scale)

    fun cellular(width: Int, height: Int, tileSize: Int): CValue<Image> =
        raylib.interop.GenImageCellular(width, height, tileSize)

    fun text(width: Int, height: Int, text: String?): CValue<Image> =
        raylib.interop.GenImageText(width, height, text)
}

object RlImageManip {
    fun copy(image: CValue<Image>): CValue<Image> = raylib.interop.ImageCopy(image)
    fun fromImage(image: CValue<Image>, rec: CValue<Rectangle>): CValue<Image> =
        raylib.interop.ImageFromImage(image, rec)

    fun text(text: String?, fontSize: Int, color: CValue<Color>): CValue<Image> =
        raylib.interop.ImageText(text, fontSize, color)

    fun textEx(
        font: CValue<Font>,
        text: String?,
        fontSize: Float,
        spacing: Float,
        tint: CValue<Color>
    ): CValue<Image> = raylib.interop.ImageTextEx(font, text, fontSize, spacing, tint)

    fun format(image: CPointer<Image>?, newFormat: Int) =
        raylib.interop.ImageFormat(image, newFormat)

    fun toPOT(image: CPointer<Image>?, fill: CValue<Color>) = raylib.interop.ImageToPOT(image, fill)
    fun crop(image: CPointer<Image>?, crop: CValue<Rectangle>) =
        raylib.interop.ImageCrop(image, crop)

    fun alphaCrop(image: CPointer<Image>?, threshold: Float) =
        raylib.interop.ImageAlphaCrop(image, threshold)

    fun alphaClear(image: CPointer<Image>?, color: CValue<Color>, threshold: Float) =
        raylib.interop.ImageAlphaClear(image, color, threshold)

    fun alphaMask(image: CPointer<Image>?, mask: CValue<Image>) =
        raylib.interop.ImageAlphaMask(image, mask)

    fun alphaPremultiply(image: CPointer<Image>?) = raylib.interop.ImageAlphaPremultiply(image)
    fun blurGaussian(image: CPointer<Image>?, blurSize: Int) =
        raylib.interop.ImageBlurGaussian(image, blurSize)

    fun resize(image: CPointer<Image>?, newWidth: Int, newHeight: Int) =
        raylib.interop.ImageResize(image, newWidth, newHeight)

    fun resizeNN(image: CPointer<Image>?, newWidth: Int, newHeight: Int) =
        raylib.interop.ImageResizeNN(image, newWidth, newHeight)

    fun resizeCanvas(
        image: CPointer<Image>?,
        newWidth: Int,
        newHeight: Int,
        offsetX: Int,
        offsetY: Int,
        fill: CValue<Color>
    ) = raylib.interop.ImageResizeCanvas(image, newWidth, newHeight, offsetX, offsetY, fill)

    fun mipmaps(image: CPointer<Image>?) = raylib.interop.ImageMipmaps(image)
    fun dither(image: CPointer<Image>?, rBpp: Int, gBpp: Int, bBpp: Int, aBpp: Int) =
        raylib.interop.ImageDither(image, rBpp, gBpp, bBpp, aBpp)

    fun flipVertical(image: CPointer<Image>?) = raylib.interop.ImageFlipVertical(image)
    fun flipHorizontal(image: CPointer<Image>?) = raylib.interop.ImageFlipHorizontal(image)
    fun rotate(image: CPointer<Image>?, degrees: Int) = raylib.interop.ImageRotate(image, degrees)
    fun rotateCW(image: CPointer<Image>?) = raylib.interop.ImageRotateCW(image)
    fun rotateCCW(image: CPointer<Image>?) = raylib.interop.ImageRotateCCW(image)
    fun colorTint(image: CPointer<Image>?, color: CValue<Color>) =
        raylib.interop.ImageColorTint(image, color)

    fun colorInvert(image: CPointer<Image>?) = raylib.interop.ImageColorInvert(image)
    fun colorGrayscale(image: CPointer<Image>?) = raylib.interop.ImageColorGrayscale(image)
    fun colorContrast(image: CPointer<Image>?, contrast: Float) =
        raylib.interop.ImageColorContrast(image, contrast)

    fun colorBrightness(image: CPointer<Image>?, brightness: Int) =
        raylib.interop.ImageColorBrightness(image, brightness)

    fun colorReplace(image: CPointer<Image>?, color: CValue<Color>, replace: CValue<Color>) =
        raylib.interop.ImageColorReplace(image, color, replace)

    fun loadColors(image: CValue<Image>): CPointer<Color>? = raylib.interop.LoadImageColors(image)
    fun loadPalette(
        image: CValue<Image>,
        maxPaletteSize: Int,
        colorCount: CPointer<IntVar>?
    ): CPointer<Color>? = raylib.interop.LoadImagePalette(image, maxPaletteSize, colorCount)

    fun unloadColors(colors: CPointer<Color>?) = raylib.interop.UnloadImageColors(colors)
    fun unloadPalette(colors: CPointer<Color>?) = raylib.interop.UnloadImagePalette(colors)
    fun alphaBorder(image: CValue<Image>, threshold: Float): CValue<Rectangle> =
        raylib.interop.GetImageAlphaBorder(image, threshold)

    fun color(image: CValue<Image>, x: Int, y: Int): CValue<Color> =
        raylib.interop.GetImageColor(image, x, y)
}

object RlImageDraw {
    fun clearBackground(dst: CPointer<Image>?, color: CValue<Color>) =
        raylib.interop.ImageClearBackground(dst, color)

    fun drawPixel(dst: CPointer<Image>?, x: Int, y: Int, color: CValue<Color>) =
        raylib.interop.ImageDrawPixel(dst, x, y, color)

    fun drawPixel(dst: CPointer<Image>?, position: CValue<Vector2>, color: CValue<Color>) =
        raylib.interop.ImageDrawPixelV(dst, position, color)

    fun drawLine(dst: CPointer<Image>?, x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>) =
        raylib.interop.ImageDrawLine(dst, x1, y1, x2, y2, color)

    fun drawLine(
        dst: CPointer<Image>?,
        start: CValue<Vector2>,
        end: CValue<Vector2>,
        color: CValue<Color>
    ) = raylib.interop.ImageDrawLineV(dst, start, end, color)

    fun drawCircle(dst: CPointer<Image>?, x: Int, y: Int, radius: Int, color: CValue<Color>) =
        raylib.interop.ImageDrawCircle(dst, x, y, radius, color)

    fun drawCircle(
        dst: CPointer<Image>?,
        center: CValue<Vector2>,
        radius: Int,
        color: CValue<Color>
    ) = raylib.interop.ImageDrawCircleV(dst, center, radius, color)

    fun drawCircleLines(dst: CPointer<Image>?, x: Int, y: Int, radius: Int, color: CValue<Color>) =
        raylib.interop.ImageDrawCircleLines(dst, x, y, radius, color)

    fun drawCircleLines(
        dst: CPointer<Image>?,
        center: CValue<Vector2>,
        radius: Int,
        color: CValue<Color>
    ) = raylib.interop.ImageDrawCircleLinesV(dst, center, radius, color)

    fun drawRectangle(
        dst: CPointer<Image>?,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color: CValue<Color>
    ) = raylib.interop.ImageDrawRectangle(dst, x, y, width, height, color)

    fun drawRectangle(
        dst: CPointer<Image>?,
        position: CValue<Vector2>,
        size: CValue<Vector2>,
        color: CValue<Color>
    ) = raylib.interop.ImageDrawRectangleV(dst, position, size, color)

    fun drawRectangleRec(dst: CPointer<Image>?, rec: CValue<Rectangle>, color: CValue<Color>) =
        raylib.interop.ImageDrawRectangleRec(dst, rec, color)

    fun drawRectangleLines(
        dst: CPointer<Image>?,
        rec: CValue<Rectangle>,
        thick: Int,
        color: CValue<Color>
    ) = raylib.interop.ImageDrawRectangleLines(dst, rec, thick, color)

    fun draw(
        dst: CPointer<Image>?,
        src: CValue<Image>,
        srcRec: CValue<Rectangle>,
        dstRec: CValue<Rectangle>,
        tint: CValue<Color>
    ) = raylib.interop.ImageDraw(dst, src, srcRec, dstRec, tint)

    fun drawText(
        dst: CPointer<Image>?,
        text: String?,
        x: Int,
        y: Int,
        fontSize: Int,
        color: CValue<Color>
    ) = raylib.interop.ImageDrawText(dst, text, x, y, fontSize, color)

    fun drawTextEx(
        dst: CPointer<Image>?,
        font: CValue<Font>,
        text: String?,
        position: CValue<Vector2>,
        fontSize: Float,
        spacing: Float,
        tint: CValue<Color>
    ) = raylib.interop.ImageDrawTextEx(dst, font, text, position, fontSize, spacing, tint)
}


object RlTextures {
    fun load(fileName: String?): CValue<Texture2D> = raylib.interop.LoadTexture(fileName)
    fun loadFromImage(image: CValue<Image>): CValue<Texture2D> =
        raylib.interop.LoadTextureFromImage(image)

    fun loadCubemap(image: CValue<Image>, layout: Int): CValue<TextureCubemap> =
        raylib.interop.LoadTextureCubemap(image, layout)

    fun loadRenderTexture(width: Int, height: Int): CValue<RenderTexture2D> =
        raylib.interop.LoadRenderTexture(width, height)

//    fun isReady(texture: CValue<Texture2D>): Boolean = raylib.interop.IsTextureReady(texture)
    fun unload(texture: CValue<Texture2D>) = raylib.interop.UnloadTexture(texture)
//    fun isRenderTextureReady(target: CValue<RenderTexture2D>): Boolean =
//        raylib.interop.IsRenderTextureReady(target)

    fun unloadRenderTexture(target: CValue<RenderTexture2D>) =
        raylib.interop.UnloadRenderTexture(target)

    fun update(texture: CValue<Texture2D>, pixels: CPointer<*>?) =
        raylib.interop.UpdateTexture(texture, pixels)

    fun updateRec(texture: CValue<Texture2D>, rec: CValue<Rectangle>, pixels: CPointer<*>?) =
        raylib.interop.UpdateTextureRec(texture, rec, pixels)
}

object RlTextureConfig {
    fun genMipmaps(texture: CPointer<Texture2D>?) = raylib.interop.GenTextureMipmaps(texture)
    fun setFilter(texture: CValue<Texture2D>, filter: Int) =
        raylib.interop.SetTextureFilter(texture, filter)

    fun setWrap(texture: CValue<Texture2D>, wrap: Int) =
        raylib.interop.SetTextureWrap(texture, wrap)
}

object RlTextureDraw {
    fun draw(texture: CValue<Texture2D>, x: Int, y: Int, tint: CValue<Color>) =
        raylib.interop.DrawTexture(texture, x, y, tint)

    fun draw(texture: CValue<Texture2D>, position: CValue<Vector2>, tint: CValue<Color>) =
        raylib.interop.DrawTextureV(texture, position, tint)

    fun drawEx(
        texture: CValue<Texture2D>,
        position: CValue<Vector2>,
        rotation: Float,
        scale: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawTextureEx(texture, position, rotation, scale, tint)

    fun drawRec(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        position: CValue<Vector2>,
        tint: CValue<Color>
    ) = raylib.interop.DrawTextureRec(texture, source, position, tint)

    fun drawPro(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        dest: CValue<Rectangle>,
        origin: CValue<Vector2>,
        rotation: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawTexturePro(texture, source, dest, origin, rotation, tint)

    fun drawNPatch(
        texture: CValue<Texture2D>,
        info: CValue<NPatchInfo>,
        dest: CValue<Rectangle>,
        origin: CValue<Vector2>,
        rotation: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawTextureNPatch(texture, info, dest, origin, rotation, tint)
}

object RlColors {
    fun fade(color: CValue<Color>, alpha: Float): CValue<Color> = raylib.interop.Fade(color, alpha)
    fun toInt(color: CValue<Color>): Int = raylib.interop.ColorToInt(color)
    fun normalize(color: CValue<Color>): CValue<Vector4> = raylib.interop.ColorNormalize(color)
    fun fromNormalized(normalized: CValue<Vector4>): CValue<Color> =
        raylib.interop.ColorFromNormalized(normalized)

    fun toHSV(color: CValue<Color>): CValue<Vector3> = raylib.interop.ColorToHSV(color)
    fun fromHSV(hue: Float, saturation: Float, value: Float): CValue<Color> =
        raylib.interop.ColorFromHSV(hue, saturation, value)

    fun tint(color: CValue<Color>, tint: CValue<Color>): CValue<Color> =
        raylib.interop.ColorTint(color, tint)

    fun brightness(color: CValue<Color>, factor: Float): CValue<Color> =
        raylib.interop.ColorBrightness(color, factor)

    fun contrast(color: CValue<Color>, contrast: Float): CValue<Color> =
        raylib.interop.ColorContrast(color, contrast)

    fun alpha(color: CValue<Color>, alpha: Float): CValue<Color> =
        raylib.interop.ColorAlpha(color, alpha)

    fun alphaBlend(dst: CValue<Color>, src: CValue<Color>, tint: CValue<Color>): CValue<Color> =
        raylib.interop.ColorAlphaBlend(dst, src, tint)

    fun get(hexValue: UInt): CValue<Color> = raylib.interop.GetColor(hexValue)
    fun getPixelColor(srcPtr: CPointer<*>?, format: Int): CValue<Color> =
        raylib.interop.GetPixelColor(srcPtr, format)

    fun setPixelColor(dstPtr: CPointer<*>?, color: CValue<Color>, format: Int) =
        raylib.interop.SetPixelColor(dstPtr, color, format)

    fun pixelDataSize(width: Int, height: Int, format: Int): Int =
        raylib.interop.GetPixelDataSize(width, height, format)
}

object RlFonts {
    fun default(): CValue<Font> = raylib.interop.GetFontDefault()
    fun load(fileName: String?): CValue<Font> = raylib.interop.LoadFont(fileName)
    fun loadEx(
        fileName: String?,
        fontSize: Int,
        codepoints: CPointer<IntVar>?,
        count: Int
    ): CValue<Font> = raylib.interop.LoadFontEx(fileName, fontSize, codepoints, count)

    fun loadFromImage(image: CValue<Image>, key: CValue<Color>, firstChar: Int): CValue<Font> =
        raylib.interop.LoadFontFromImage(image, key, firstChar)

    fun loadFromMemory(
        fileType: String?,
        data: CPointer<UByteVar>?,
        dataSize: Int,
        fontSize: Int,
        codepoints: CPointer<IntVar>?,
        count: Int
    ): CValue<Font> =
        raylib.interop.LoadFontFromMemory(fileType, data, dataSize, fontSize, codepoints, count)

//    fun isReady(font: CValue<Font>): Boolean = raylib.interop.IsFontReady(font)
//    fun loadData(
//        data: CPointer<UByteVar>?,
//        dataSize: Int,
//        fontSize: Int,
//        codepoints: CPointer<IntVar>?,
//        count: Int,
//        type: Int
//    ): CPointer<GlyphInfo>? =
//        raylib.interop.LoadFontData(data, dataSize, fontSize, codepoints, count, type)

    //    fun genAtlas(glyphs: CPointer<GlyphInfo>?, glyphRecs: CValuesRef<CPointerVarOf<CPointer<Rectangle>>?, glyphCount: Int, fontSize: Int, padding: Int, packMethod: Int): CValue<Image> = raylib.interop.GenImageFontAtlas(glyphs, glyphRecs, glyphCount, fontSize, padding, packMethod)
    fun unloadData(glyphs: CPointer<GlyphInfo>?, glyphCount: Int) =
        raylib.interop.UnloadFontData(glyphs, glyphCount)

    fun unload(font: CValue<Font>) = raylib.interop.UnloadFont(font)
    fun exportAsCode(font: CValue<Font>, fileName: String?): Boolean =
        raylib.interop.ExportFontAsCode(font, fileName)
}

object TextDrawScope {
    fun drawFPS(x: Int, y: Int) = raylib.interop.DrawFPS(x, y)
    fun draw(text: String, x: Int, y: Int, fontSize: Int, color: CValue<Color>) =
        raylib.interop.DrawText(text, x, y, fontSize, color)

    fun drawEx(
        font: CValue<Font>,
        text: String?,
        position: CValue<Vector2>,
        fontSize: Float,
        spacing: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawTextEx(font, text, position, fontSize, spacing, tint)

    fun drawPro(
        font: CValue<Font>,
        text: String?,
        position: CValue<Vector2>,
        origin: CValue<Vector2>,
        rotation: Float,
        fontSize: Float,
        spacing: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawTextPro(font, text, position, origin, rotation, fontSize, spacing, tint)

    fun drawCodepoint(
        font: CValue<Font>,
        codepoint: Int,
        position: CValue<Vector2>,
        fontSize: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawTextCodepoint(font, codepoint, position, fontSize, tint)

    fun drawCodepoints(
        font: CValue<Font>,
        codepoints: CPointer<IntVar>?,
        count: Int,
        position: CValue<Vector2>,
        fontSize: Float,
        spacing: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawTextCodepoints(
        font,
        codepoints,
        count,
        position,
        fontSize,
        spacing,
        tint
    )
}

object RlTextMeasure {
    fun setLineSpacing(spacing: Int) = raylib.interop.SetTextLineSpacing(spacing)
    fun measure(text: String?, fontSize: Int): Int = raylib.interop.MeasureText(text, fontSize)
    fun measureEx(
        font: CValue<Font>,
        text: String?,
        fontSize: Float,
        spacing: Float
    ): CValue<Vector2> = raylib.interop.MeasureTextEx(font, text, fontSize, spacing)

    fun glyphIndex(font: CValue<Font>, codepoint: Int): Int =
        raylib.interop.GetGlyphIndex(font, codepoint)

    fun glyphInfo(font: CValue<Font>, codepoint: Int): CValue<GlyphInfo> =
        raylib.interop.GetGlyphInfo(font, codepoint)

    fun glyphAtlasRec(font: CValue<Font>, codepoint: Int): CValue<Rectangle> =
        raylib.interop.GetGlyphAtlasRec(font, codepoint)
}

object RlCodepoints {
    fun loadUTF8(codepoints: CPointer<IntVar>?, length: Int): String? =
        raylib.interop.LoadUTF8(codepoints, length)?.toKString()

    fun unloadUTF8(text: String) = raylib.interop.UnloadUTF8(text.cstr)
    fun load(text: String?, count: CPointer<IntVar>?): CPointer<IntVar>? =
        raylib.interop.LoadCodepoints(text, count)

    fun unload(codepoints: CPointer<IntVar>?) = raylib.interop.UnloadCodepoints(codepoints)
    fun count(text: String?): Int = raylib.interop.GetCodepointCount(text)
    fun next(text: String?, size: CPointer<IntVar>?): Int =
        raylib.interop.GetCodepointNext(text, size)

    fun previous(text: String?, size: CPointer<IntVar>?): Int =
        raylib.interop.GetCodepointPrevious(text, size)

    fun toUTF8(codepoint: Int, size: CPointer<IntVar>?): String? =
        raylib.interop.CodepointToUTF8(codepoint, size)?.toKString()
}

object RlText {
    fun copy(dst: CPointer<ByteVar>, src: String): Int = raylib.interop.TextCopy(dst, src)
    fun isEqual(t1: String?, t2: String?): Boolean = raylib.interop.TextIsEqual(t1, t2)
    fun length(text: String?): UInt = raylib.interop.TextLength(text)
    fun subtext(text: String?, position: Int, length: Int): String? =
        raylib.interop.TextSubtext(text, position, length)?.toKString()

//    fun replace(text: String, replace: String?, by: String?): String? =
//        raylib.interop.TextReplace(text.cstr, replace, by)?.toKString()

    fun insert(text: String?, insert: String?, position: Int): String? =
        raylib.interop.TextInsert(text, insert, position)?.toKString()

    fun findIndex(text: String?, find: String?): Int = raylib.interop.TextFindIndex(text, find)
    fun toUpper(text: String): String? = raylib.interop.TextToUpper(text)?.toKString()
    fun toLower(text: String): String? = raylib.interop.TextToLower(text)?.toKString()
    fun toPascal(text: String): String? = raylib.interop.TextToPascal(text)?.toKString()
    fun toInteger(text: String): Int = raylib.interop.TextToInteger(text)
}


object RlShapes3D {
    fun drawLine(start: CValue<Vector3>, end: CValue<Vector3>, color: CValue<Color>) =
        raylib.interop.DrawLine3D(start, end, color)

    fun drawPoint(position: CValue<Vector3>, color: CValue<Color>) =
        raylib.interop.DrawPoint3D(position, color)

    fun drawCircle(
        center: CValue<Vector3>,
        radius: Float,
        rotationAxis: CValue<Vector3>,
        rotationAngle: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawCircle3D(center, radius, rotationAxis, rotationAngle, color)

    fun drawTriangle(
        v1: CValue<Vector3>,
        v2: CValue<Vector3>,
        v3: CValue<Vector3>,
        color: CValue<Color>
    ) = raylib.interop.DrawTriangle3D(v1, v2, v3, color)

    fun drawTriangleStrip(points: CPointer<Vector3>?, pointCount: Int, color: CValue<Color>) =
        raylib.interop.DrawTriangleStrip3D(points, pointCount, color)

    fun drawCube(
        position: CValue<Vector3>,
        width: Float,
        height: Float,
        length: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawCube(position, width, height, length, color)

    fun drawCube(position: CValue<Vector3>, size: CValue<Vector3>, color: CValue<Color>) =
        raylib.interop.DrawCubeV(position, size, color)

    fun drawCubeWires(
        position: CValue<Vector3>,
        width: Float,
        height: Float,
        length: Float,
        color: CValue<Color>
    ) = raylib.interop.DrawCubeWires(position, width, height, length, color)

    fun drawCubeWires(position: CValue<Vector3>, size: CValue<Vector3>, color: CValue<Color>) =
        raylib.interop.DrawCubeWiresV(position, size, color)

    fun drawSphere(center: CValue<Vector3>, radius: Float, color: CValue<Color>) =
        raylib.interop.DrawSphere(center, radius, color)

    fun drawSphereEx(
        center: CValue<Vector3>,
        radius: Float,
        rings: Int,
        slices: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawSphereEx(center, radius, rings, slices, color)

    fun drawSphereWires(
        center: CValue<Vector3>,
        radius: Float,
        rings: Int,
        slices: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawSphereWires(center, radius, rings, slices, color)

    fun drawCylinder(
        position: CValue<Vector3>,
        radiusTop: Float,
        radiusBottom: Float,
        height: Float,
        slices: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCylinder(position, radiusTop, radiusBottom, height, slices, color)

    fun drawCylinderEx(
        start: CValue<Vector3>,
        end: CValue<Vector3>,
        startRadius: Float,
        endRadius: Float,
        sides: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCylinderEx(start, end, startRadius, endRadius, sides, color)

    fun drawCylinderWires(
        position: CValue<Vector3>,
        radiusTop: Float,
        radiusBottom: Float,
        height: Float,
        slices: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCylinderWires(position, radiusTop, radiusBottom, height, slices, color)

    fun drawCylinderWiresEx(
        start: CValue<Vector3>,
        end: CValue<Vector3>,
        startRadius: Float,
        endRadius: Float,
        sides: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCylinderWiresEx(start, end, startRadius, endRadius, sides, color)

    fun drawCapsule(
        start: CValue<Vector3>,
        end: CValue<Vector3>,
        radius: Float,
        slices: Int,
        rings: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCapsule(start, end, radius, slices, rings, color)

    fun drawCapsuleWires(
        start: CValue<Vector3>,
        end: CValue<Vector3>,
        radius: Float,
        slices: Int,
        rings: Int,
        color: CValue<Color>
    ) = raylib.interop.DrawCapsuleWires(start, end, radius, slices, rings, color)

    fun drawPlane(center: CValue<Vector3>, size: CValue<Vector2>, color: CValue<Color>) =
        raylib.interop.DrawPlane(center, size, color)

    fun drawRay(ray: CValue<Ray>, color: CValue<Color>) = raylib.interop.DrawRay(ray, color)
    fun drawGrid(slices: Int, spacing: Float) = raylib.interop.DrawGrid(slices, spacing)
}

object RlModels {
    fun load(fileName: String?): CValue<Model> = raylib.interop.LoadModel(fileName)
    fun loadFromMesh(mesh: CValue<Mesh>): CValue<Model> = raylib.interop.LoadModelFromMesh(mesh)
//    fun isReady(model: CValue<Model>): Boolean = raylib.interop.IsModelReady(model)
    fun unload(model: CValue<Model>) = raylib.interop.UnloadModel(model)
    fun boundingBox(model: CValue<Model>): CValue<BoundingBox> =
        raylib.interop.GetModelBoundingBox(model)

    fun draw(model: CValue<Model>, position: CValue<Vector3>, scale: Float, tint: CValue<Color>) =
        raylib.interop.DrawModel(model, position, scale, tint)

    fun drawEx(
        model: CValue<Model>,
        position: CValue<Vector3>,
        rotationAxis: CValue<Vector3>,
        rotationAngle: Float,
        scale: CValue<Vector3>,
        tint: CValue<Color>
    ) = raylib.interop.DrawModelEx(model, position, rotationAxis, rotationAngle, scale, tint)

    fun drawWires(
        model: CValue<Model>,
        position: CValue<Vector3>,
        scale: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawModelWires(model, position, scale, tint)

    fun drawWiresEx(
        model: CValue<Model>,
        position: CValue<Vector3>,
        rotationAxis: CValue<Vector3>,
        rotationAngle: Float,
        scale: CValue<Vector3>,
        tint: CValue<Color>
    ) = raylib.interop.DrawModelWiresEx(model, position, rotationAxis, rotationAngle, scale, tint)

    fun drawBoundingBox(box: CValue<BoundingBox>, color: CValue<Color>) =
        raylib.interop.DrawBoundingBox(box, color)

    fun drawBillboard(
        camera: CValue<Camera>,
        texture: CValue<Texture2D>,
        position: CValue<Vector3>,
        size: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawBillboard(camera, texture, position, size, tint)

    fun drawBillboardRec(
        camera: CValue<Camera>,
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        position: CValue<Vector3>,
        size: CValue<Vector2>,
        tint: CValue<Color>
    ) = raylib.interop.DrawBillboardRec(camera, texture, source, position, size, tint)

    fun drawBillboardPro(
        camera: CValue<Camera>,
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        position: CValue<Vector3>,
        up: CValue<Vector3>,
        size: CValue<Vector2>,
        origin: CValue<Vector2>,
        rotation: Float,
        tint: CValue<Color>
    ) = raylib.interop.DrawBillboardPro(
        camera,
        texture,
        source,
        position,
        up,
        size,
        origin,
        rotation,
        tint
    )
}

object RlMeshes {
    fun upload(mesh: CPointer<Mesh>?, dynamic: Boolean) = raylib.interop.UploadMesh(mesh, dynamic)
    fun updateBuffer(
        mesh: CValue<Mesh>,
        index: Int,
        data: CPointer<*>?,
        dataSize: Int,
        offset: Int
    ) = raylib.interop.UpdateMeshBuffer(mesh, index, data, dataSize, offset)

    fun unload(mesh: CValue<Mesh>) = raylib.interop.UnloadMesh(mesh)
    fun draw(mesh: CValue<Mesh>, material: CValue<Material>, transform: CValue<Matrix>) =
        raylib.interop.DrawMesh(mesh, material, transform)

    fun drawInstanced(
        mesh: CValue<Mesh>,
        material: CValue<Material>,
        transforms: CPointer<Matrix>?,
        instances: Int
    ) = raylib.interop.DrawMeshInstanced(mesh, material, transforms, instances)

    fun export(mesh: CValue<Mesh>, fileName: String?): Boolean =
        raylib.interop.ExportMesh(mesh, fileName)

    fun boundingBox(mesh: CValue<Mesh>): CValue<BoundingBox> =
        raylib.interop.GetMeshBoundingBox(mesh)

    fun genTangents(mesh: CPointer<Mesh>?) = raylib.interop.GenMeshTangents(mesh)
}

object RlMeshGen {
    fun poly(sides: Int, radius: Float): CValue<Mesh> = raylib.interop.GenMeshPoly(sides, radius)
    fun plane(width: Float, length: Float, resX: Int, resZ: Int): CValue<Mesh> =
        raylib.interop.GenMeshPlane(width, length, resX, resZ)

    fun cube(width: Float, height: Float, length: Float): CValue<Mesh> =
        raylib.interop.GenMeshCube(width, height, length)

    fun sphere(radius: Float, rings: Int, slices: Int): CValue<Mesh> =
        raylib.interop.GenMeshSphere(radius, rings, slices)

    fun hemiSphere(radius: Float, rings: Int, slices: Int): CValue<Mesh> =
        raylib.interop.GenMeshHemiSphere(radius, rings, slices)

    fun cylinder(radius: Float, height: Float, slices: Int): CValue<Mesh> =
        raylib.interop.GenMeshCylinder(radius, height, slices)

    fun cone(radius: Float, height: Float, slices: Int): CValue<Mesh> =
        raylib.interop.GenMeshCone(radius, height, slices)

    fun torus(radius: Float, size: Float, radSeg: Int, sides: Int): CValue<Mesh> =
        raylib.interop.GenMeshTorus(radius, size, radSeg, sides)

    fun knot(radius: Float, size: Float, radSeg: Int, sides: Int): CValue<Mesh> =
        raylib.interop.GenMeshKnot(radius, size, radSeg, sides)

    fun heightmap(heightmap: CValue<Image>, size: CValue<Vector3>): CValue<Mesh> =
        raylib.interop.GenMeshHeightmap(heightmap, size)

    fun cubicmap(cubicmap: CValue<Image>, cubeSize: CValue<Vector3>): CValue<Mesh> =
        raylib.interop.GenMeshCubicmap(cubicmap, cubeSize)
}

object RlMaterials {
    fun load(fileName: String?, materialCount: CPointer<IntVar>?): CPointer<Material>? =
        raylib.interop.LoadMaterials(fileName, materialCount)

    fun default(): CValue<Material> = raylib.interop.LoadMaterialDefault()
//    fun isReady(material: CValue<Material>): Boolean = raylib.interop.IsMaterialReady(material)
    fun unload(material: CValue<Material>) = raylib.interop.UnloadMaterial(material)
    fun setTexture(material: CPointer<Material>?, mapType: Int, texture: CValue<Texture2D>) =
        raylib.interop.SetMaterialTexture(material, mapType, texture)

    fun setModelMeshMaterial(model: CPointer<Model>?, meshId: Int, materialId: Int) =
        raylib.interop.SetModelMeshMaterial(model, meshId, materialId)
}

object RlModelAnimations {
    fun load(fileName: String?, animCount: CPointer<IntVar>?): CPointer<ModelAnimation>? =
        raylib.interop.LoadModelAnimations(fileName, animCount)

    fun update(model: CValue<Model>, anim: CValue<ModelAnimation>, frame: Int) =
        raylib.interop.UpdateModelAnimation(model, anim, frame)

    fun unload(anim: CValue<ModelAnimation>) = raylib.interop.UnloadModelAnimation(anim)
    fun unloadAll(anims: CPointer<ModelAnimation>?, animCount: Int) =
        raylib.interop.UnloadModelAnimations(anims, animCount)

    fun isValid(model: CValue<Model>, anim: CValue<ModelAnimation>): Boolean =
        raylib.interop.IsModelAnimationValid(model, anim)
}

object RlCollisions3D {
    fun spheres(c1: CValue<Vector3>, r1: Float, c2: CValue<Vector3>, r2: Float): Boolean =
        raylib.interop.CheckCollisionSpheres(c1, r1, c2, r2)

    fun boxes(b1: CValue<BoundingBox>, b2: CValue<BoundingBox>): Boolean =
        raylib.interop.CheckCollisionBoxes(b1, b2)

    fun boxSphere(box: CValue<BoundingBox>, center: CValue<Vector3>, radius: Float): Boolean =
        raylib.interop.CheckCollisionBoxSphere(box, center, radius)

    fun raySphere(ray: CValue<Ray>, center: CValue<Vector3>, radius: Float): CValue<RayCollision> =
        raylib.interop.GetRayCollisionSphere(ray, center, radius)

    fun rayBox(ray: CValue<Ray>, box: CValue<BoundingBox>): CValue<RayCollision> =
        raylib.interop.GetRayCollisionBox(ray, box)

    fun rayMesh(
        ray: CValue<Ray>,
        mesh: CValue<Mesh>,
        transform: CValue<Matrix>
    ): CValue<RayCollision> = raylib.interop.GetRayCollisionMesh(ray, mesh, transform)

    fun rayTriangle(
        ray: CValue<Ray>,
        p1: CValue<Vector3>,
        p2: CValue<Vector3>,
        p3: CValue<Vector3>
    ): CValue<RayCollision> = raylib.interop.GetRayCollisionTriangle(ray, p1, p2, p3)

    fun rayQuad(
        ray: CValue<Ray>,
        p1: CValue<Vector3>,
        p2: CValue<Vector3>,
        p3: CValue<Vector3>,
        p4: CValue<Vector3>
    ): CValue<RayCollision> = raylib.interop.GetRayCollisionQuad(ray, p1, p2, p3, p4)
}

object RlAudioDevice {
    fun init() = raylib.interop.InitAudioDevice()
    fun close() = raylib.interop.CloseAudioDevice()
    fun isReady(): Boolean = raylib.interop.IsAudioDeviceReady()
    fun setMasterVolume(volume: Float) = raylib.interop.SetMasterVolume(volume)
    fun masterVolume(): Float = raylib.interop.GetMasterVolume()
}

object RlWavesAndSounds {
    fun loadWave(fileName: String?): CValue<Wave> = raylib.interop.LoadWave(fileName)

    //    fun loadWaveFromMemory(fileType: String?, data: CPointer<ByteVar>?, dataSize: Int): CValue<Wave> = raylib.interop.LoadWaveFromMemory(fileType, data, dataSize)
//    fun isWaveReady(wave: CValue<Wave>): Boolean = raylib.interop.IsWaveReady(wave)
    fun loadSound(fileName: String?): CValue<Sound> = raylib.interop.LoadSound(fileName)
    fun loadSoundFromWave(wave: CValue<Wave>): CValue<Sound> =
        raylib.interop.LoadSoundFromWave(wave)

    fun loadSoundAlias(source: CValue<Sound>): CValue<Sound> = raylib.interop.LoadSoundAlias(source)
//    fun isSoundReady(sound: CValue<Sound>): Boolean = raylib.interop.IsSoundReady(sound)
    fun updateSound(sound: CValue<Sound>, data: CPointer<*>?, sampleCount: Int) =
        raylib.interop.UpdateSound(sound, data, sampleCount)

    fun unloadWave(wave: CValue<Wave>) = raylib.interop.UnloadWave(wave)
    fun unloadSound(sound: CValue<Sound>) = raylib.interop.UnloadSound(sound)
    fun unloadSoundAlias(alias: CValue<Sound>) = raylib.interop.UnloadSoundAlias(alias)
    fun exportWave(wave: CValue<Wave>, fileName: String?): Boolean =
        raylib.interop.ExportWave(wave, fileName)

    fun exportWaveAsCode(wave: CValue<Wave>, fileName: String?): Boolean =
        raylib.interop.ExportWaveAsCode(wave, fileName)

    fun play(sound: CValue<Sound>) = raylib.interop.PlaySound(sound)
    fun stop(sound: CValue<Sound>) = raylib.interop.StopSound(sound)
    fun pause(sound: CValue<Sound>) = raylib.interop.PauseSound(sound)
    fun resume(sound: CValue<Sound>) = raylib.interop.ResumeSound(sound)
    fun isPlaying(sound: CValue<Sound>): Boolean = raylib.interop.IsSoundPlaying(sound)
    fun setVolume(sound: CValue<Sound>, volume: Float) =
        raylib.interop.SetSoundVolume(sound, volume)

    fun setPitch(sound: CValue<Sound>, pitch: Float) = raylib.interop.SetSoundPitch(sound, pitch)
    fun setPan(sound: CValue<Sound>, pan: Float) = raylib.interop.SetSoundPan(sound, pan)
    fun waveCopy(wave: CValue<Wave>): CValue<Wave> = raylib.interop.WaveCopy(wave)
    fun waveCrop(wave: CPointer<Wave>?, initSample: Int, finalSample: Int) =
        raylib.interop.WaveCrop(wave, initSample, finalSample)

    fun waveFormat(wave: CPointer<Wave>?, sampleRate: Int, sampleSize: Int, channels: Int) =
        raylib.interop.WaveFormat(wave, sampleRate, sampleSize, channels)

    fun loadSamples(wave: CValue<Wave>): CPointer<FloatVar>? = raylib.interop.LoadWaveSamples(wave)
    fun unloadSamples(samples: CPointer<FloatVar>?) = raylib.interop.UnloadWaveSamples(samples)
}

object RlMusic {
    fun loadStream(fileName: String?): CValue<Music> = raylib.interop.LoadMusicStream(fileName)

    //    fun loadStreamFromMemory(fileType: String?, data: CPointer<ByteVar>?, dataSize: Int): CValue<Music> = raylib.interop.LoadMusicStreamFromMemory(fileType, data, dataSize)
//    fun isReady(music: CValue<Music>): Boolean = raylib.interop.IsMusicReady(music)
    fun unloadStream(music: CValue<Music>) = raylib.interop.UnloadMusicStream(music)
    fun play(music: CValue<Music>) = raylib.interop.PlayMusicStream(music)
    fun isPlaying(music: CValue<Music>): Boolean = raylib.interop.IsMusicStreamPlaying(music)
    fun update(music: CValue<Music>) = raylib.interop.UpdateMusicStream(music)
    fun stop(music: CValue<Music>) = raylib.interop.StopMusicStream(music)
    fun pause(music: CValue<Music>) = raylib.interop.PauseMusicStream(music)
    fun resume(music: CValue<Music>) = raylib.interop.ResumeMusicStream(music)
    fun seek(music: CValue<Music>, positionSeconds: Float) =
        raylib.interop.SeekMusicStream(music, positionSeconds)

    fun setVolume(music: CValue<Music>, volume: Float) =
        raylib.interop.SetMusicVolume(music, volume)

    fun setPitch(music: CValue<Music>, pitch: Float) = raylib.interop.SetMusicPitch(music, pitch)
    fun setPan(music: CValue<Music>, pan: Float) = raylib.interop.SetMusicPan(music, pan)
    fun timeLength(music: CValue<Music>): Float = raylib.interop.GetMusicTimeLength(music)
    fun timePlayed(music: CValue<Music>): Float = raylib.interop.GetMusicTimePlayed(music)
}

object RlAudioStream {
    fun load(sampleRate: UInt, sampleSize: UInt, channels: UInt): CValue<AudioStream> =
        raylib.interop.LoadAudioStream(sampleRate, sampleSize, channels)

//    fun isReady(stream: CValue<AudioStream>): Boolean = raylib.interop.IsAudioStreamReady(stream)
    fun unload(stream: CValue<AudioStream>) = raylib.interop.UnloadAudioStream(stream)
    fun update(stream: CValue<AudioStream>, data: CPointer<*>?, frameCount: Int) =
        raylib.interop.UpdateAudioStream(stream, data, frameCount)

    fun isProcessed(stream: CValue<AudioStream>): Boolean =
        raylib.interop.IsAudioStreamProcessed(stream)

    fun play(stream: CValue<AudioStream>) = raylib.interop.PlayAudioStream(stream)
    fun pause(stream: CValue<AudioStream>) = raylib.interop.PauseAudioStream(stream)
    fun resume(stream: CValue<AudioStream>) = raylib.interop.ResumeAudioStream(stream)
    fun isPlaying(stream: CValue<AudioStream>): Boolean =
        raylib.interop.IsAudioStreamPlaying(stream)

    fun stop(stream: CValue<AudioStream>) = raylib.interop.StopAudioStream(stream)
    fun setVolume(stream: CValue<AudioStream>, volume: Float) =
        raylib.interop.SetAudioStreamVolume(stream, volume)

    fun setPitch(stream: CValue<AudioStream>, pitch: Float) =
        raylib.interop.SetAudioStreamPitch(stream, pitch)

    fun setPan(stream: CValue<AudioStream>, pan: Float) =
        raylib.interop.SetAudioStreamPan(stream, pan)

    fun setBufferSizeDefault(size: Int) = raylib.interop.SetAudioStreamBufferSizeDefault(size)
    fun setCallback(stream: CValue<AudioStream>, callback: AudioCallback?) =
        raylib.interop.SetAudioStreamCallback(stream, callback)

    fun attachProcessor(stream: CValue<AudioStream>, processor: AudioCallback?) =
        raylib.interop.AttachAudioStreamProcessor(stream, processor)

    fun detachProcessor(stream: CValue<AudioStream>, processor: AudioCallback?) =
        raylib.interop.DetachAudioStreamProcessor(stream, processor)

    fun attachMixedProcessor(processor: AudioCallback?) =
        raylib.interop.AttachAudioMixedProcessor(processor)

    fun detachMixedProcessor(processor: AudioCallback?) =
        raylib.interop.DetachAudioMixedProcessor(processor)
}