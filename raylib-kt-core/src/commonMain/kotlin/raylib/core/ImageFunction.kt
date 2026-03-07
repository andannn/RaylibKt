package raylib.core

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.FloatVar

interface ImageFunction {
    fun loadImage(fileName: String): CValue<Image>
    fun unLoadImage(image: CValue<Image>)

    fun imageCrop(imagePtr: CPointer<Image>, crop: CValue<Rectangle>)
    fun imageAlphaCrop(imagePtr: CPointer<Image>, threshold: Float)
    fun imageAlphaClear(imagePtr: CPointer<Image>, color: CValue<Color>, threshold: Float)
    fun imageAlphaMask(imagePtr: CPointer<Image>, alphaMask: CValue<Image>)
    fun imageAlphaPremultiply(imagePtr: CPointer<Image>)
    fun imageBlurGaussian(imagePtr: CPointer<Image>, blurSize: Int)

    fun imageKernelConvolution(imagePtr: CPointer<Image>, kernel: CPointer<FloatVar>, kernelSize: Int)
    fun imageResize(imagePtr: CPointer<Image>, newWidth: Int, newHeight: Int)
    fun imageResizeNN(imagePtr: CPointer<Image>, newWidth: Int, newHeight: Int)
    fun imageResizeCanvas(
        imagePtr: CPointer<Image>,
        newWidth: Int,
        newHeight: Int,
        offsetX: Int,
        offsetY: Int,
        fill: CValue<Color>
    )

    fun imageMipmaps(imagePtr: CPointer<Image>)
    fun imageDither(imagePtr: CPointer<Image>, rBpp: Int, gBpp: Int, bBpp: Int, aBpp: Int)
    fun imageFlipVertical(imagePtr: CPointer<Image>)
    fun imageFlipHorizontal(imagePtr: CPointer<Image>)
    fun imageRotate(imagePtr: CPointer<Image>, degrees: Int)
    fun imageRotateCW(imagePtr: CPointer<Image>)
    fun imageRotateCCW(imagePtr: CPointer<Image>)

    fun imageColorTint(imagePtr: CPointer<Image>, color: CValue<Color>)
    fun imageColorInvert(imagePtr: CPointer<Image>)
    fun imageColorGrayscale(imagePtr: CPointer<Image>)
    fun imageColorContrast(imagePtr: CPointer<Image>, contrast: Float)
    fun imageColorBrightness(imagePtr: CPointer<Image>, brightness: Int)
    fun imageColorReplace(imagePtr: CPointer<Image>, color: CValue<Color>, replace: CValue<Color>)

    fun imageClearBackground(imagePtr: CPointer<Image>, color: CValue<Color>)
    fun imageDrawPixel(imagePtr: CPointer<Image>, posX: Int, posY: Int, color: CValue<Color>)
    fun imageDrawPixelV(imagePtr: CPointer<Image>, position: CValue<Vector2>, color: CValue<Color>)

    fun imageDrawLine(imagePtr: CPointer<Image>, startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: CValue<Color>)
    fun imageDrawLineV(imagePtr: CPointer<Image>, start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>)
    fun imageDrawLineEx(imagePtr: CPointer<Image>, start: CValue<Vector2>, end: CValue<Vector2>, thick: Int, color: CValue<Color>)

    fun imageDrawCircle(imagePtr: CPointer<Image>, centerX: Int, centerY: Int, radius: Int, color: CValue<Color>)
    fun imageDrawCircleV(imagePtr: CPointer<Image>, center: CValue<Vector2>, radius: Int, color: CValue<Color>)
    fun imageDrawCircleLines(imagePtr: CPointer<Image>, centerX: Int, centerY: Int, radius: Int, color: CValue<Color>)
    fun imageDrawCircleLinesV(imagePtr: CPointer<Image>, center: CValue<Vector2>, radius: Int, color: CValue<Color>)

    fun imageDrawRectangle(imagePtr: CPointer<Image>, posX: Int, posY: Int, width: Int, height: Int, color: CValue<Color>)
    fun imageDrawRectangleV(imagePtr: CPointer<Image>, position: CValue<Vector2>, size: CValue<Vector2>, color: CValue<Color>)
    fun imageDrawRectangleRec(imagePtr: CPointer<Image>, rec: CValue<Rectangle>, color: CValue<Color>)
    fun imageDrawRectangleLines(imagePtr: CPointer<Image>, rec: CValue<Rectangle>, thick: Int, color: CValue<Color>)

    fun imageDrawTriangle(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>)
    fun imageDrawTriangleEx(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, c1: CValue<Color>, c2: CValue<Color>, c3: CValue<Color>)
    fun imageDrawTriangleLines(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>)
    fun imageDrawTriangleFan(imagePtr: CPointer<Image>, points: CPointer<Vector2>, pointCount: Int, color: CValue<Color>)
    fun imageDrawTriangleStrip(imagePtr: CPointer<Image>, points: CPointer<Vector2>, pointCount: Int, color: CValue<Color>)

    // --- 图像合并与文本绘制 ---
    fun imageDraw(imagePtr: CPointer<Image>, image: CValue<Image>, srcRec: CValue<Rectangle>, dstRec: CValue<Rectangle>, tint: CValue<Color>)
    fun imageDrawText(imagePtr: CPointer<Image>, text: String, posX: Int, posY: Int, fontSize: Int, color: CValue<Color>)
    fun imageDrawTextEx(imagePtr: CPointer<Image>, font: CValue<Font>, text: String, position: CValue<Vector2>, fontSize: Float, spacing: Float, tint: CValue<Color>)
}

fun ImageFunction(): ImageFunction = DefaultImageFunction()

private class DefaultImageFunction : ImageFunction {
    override fun loadImage(fileName: String): CValue<Image> {
        return raylib.interop.LoadImage(fileName)
    }

    override fun unLoadImage(image: CValue<Image>) {
        raylib.interop.UnloadImage(image)
    }

    override fun imageCrop(imagePtr: CPointer<Image>, crop: CValue<Rectangle>) {
        raylib.interop.ImageCrop(imagePtr, crop)
    }

    override fun imageAlphaCrop(imagePtr: CPointer<Image>, threshold: Float) {
        raylib.interop.ImageAlphaCrop(imagePtr, threshold)
    }

    override fun imageAlphaClear(imagePtr: CPointer<Image>, color: CValue<Color>, threshold: Float) {
        raylib.interop.ImageAlphaClear(imagePtr, color, threshold)
    }

    override fun imageAlphaMask(imagePtr: CPointer<Image>, alphaMask: CValue<Image>) {
        raylib.interop.ImageAlphaMask(imagePtr, alphaMask)
    }

    override fun imageAlphaPremultiply(imagePtr: CPointer<Image>) {
        raylib.interop.ImageAlphaPremultiply(imagePtr)
    }

    override fun imageBlurGaussian(imagePtr: CPointer<Image>, blurSize: Int) {
        raylib.interop.ImageBlurGaussian(imagePtr, blurSize)
    }

    override fun imageKernelConvolution(imagePtr: CPointer<Image>, kernel: CPointer<FloatVar>, kernelSize: Int) {
        raylib.interop.ImageKernelConvolution(imagePtr, kernel, kernelSize)
    }

    override fun imageResize(imagePtr: CPointer<Image>, newWidth: Int, newHeight: Int) {
        raylib.interop.ImageResize(imagePtr, newWidth, newHeight)
    }

    override fun imageResizeNN(imagePtr: CPointer<Image>, newWidth: Int, newHeight: Int) {
        raylib.interop.ImageResizeNN(imagePtr, newWidth, newHeight)
    }

    override fun imageResizeCanvas(
        imagePtr: CPointer<Image>,
        newWidth: Int,
        newHeight: Int,
        offsetX: Int,
        offsetY: Int,
        fill: CValue<Color>
    ) {
        raylib.interop.ImageResizeCanvas(imagePtr, newWidth, newHeight, offsetX, offsetY, fill)
    }

    override fun imageMipmaps(imagePtr: CPointer<Image>) {
        raylib.interop.ImageMipmaps(imagePtr)
    }

    override fun imageDither(imagePtr: CPointer<Image>, rBpp: Int, gBpp: Int, bBpp: Int, aBpp: Int) {
        raylib.interop.ImageDither(imagePtr, rBpp, gBpp, bBpp, aBpp)
    }

    override fun imageFlipVertical(imagePtr: CPointer<Image>) {
        raylib.interop.ImageFlipVertical(imagePtr)
    }

    override fun imageFlipHorizontal(imagePtr: CPointer<Image>) {
        raylib.interop.ImageFlipHorizontal(imagePtr)
    }

    override fun imageRotate(imagePtr: CPointer<Image>, degrees: Int) {
        raylib.interop.ImageRotate(imagePtr, degrees)
    }

    override fun imageRotateCW(imagePtr: CPointer<Image>) {
        raylib.interop.ImageRotateCW(imagePtr)
    }

    override fun imageRotateCCW(imagePtr: CPointer<Image>) {
        raylib.interop.ImageRotateCCW(imagePtr)
    }

    override fun imageColorTint(imagePtr: CPointer<Image>, color: CValue<Color>) {
        raylib.interop.ImageColorTint(imagePtr, color)
    }

    override fun imageColorInvert(imagePtr: CPointer<Image>) {
        raylib.interop.ImageColorInvert(imagePtr)
    }

    override fun imageColorGrayscale(imagePtr: CPointer<Image>) {
        raylib.interop.ImageColorGrayscale(imagePtr)
    }

    override fun imageColorContrast(imagePtr: CPointer<Image>, contrast: Float) {
        raylib.interop.ImageColorContrast(imagePtr, contrast)
    }

    override fun imageColorBrightness(imagePtr: CPointer<Image>, brightness: Int) {
        raylib.interop.ImageColorBrightness(imagePtr, brightness)
    }

    override fun imageColorReplace(imagePtr: CPointer<Image>, color: CValue<Color>, replace: CValue<Color>) {
        raylib.interop.ImageColorReplace(imagePtr, color, replace)
    }


    override fun imageClearBackground(imagePtr: CPointer<Image>, color: CValue<Color>) = raylib.interop.ImageClearBackground(imagePtr, color)
    override fun imageDrawPixel(imagePtr: CPointer<Image>, posX: Int, posY: Int, color: CValue<Color>) = raylib.interop.ImageDrawPixel(imagePtr, posX, posY, color)
    override fun imageDrawPixelV(imagePtr: CPointer<Image>, position: CValue<Vector2>, color: CValue<Color>) = raylib.interop.ImageDrawPixelV(imagePtr, position, color)

    override fun imageDrawLine(imagePtr: CPointer<Image>, startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: CValue<Color>) = raylib.interop.ImageDrawLine(imagePtr, startPosX, startPosY, endPosX, endPosY, color)
    override fun imageDrawLineV(imagePtr: CPointer<Image>, start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>) = raylib.interop.ImageDrawLineV(imagePtr, start, end, color)
    override fun imageDrawLineEx(imagePtr: CPointer<Image>, start: CValue<Vector2>, end: CValue<Vector2>, thick: Int, color: CValue<Color>) = raylib.interop.ImageDrawLineEx(imagePtr, start, end, thick, color)

    override fun imageDrawCircle(imagePtr: CPointer<Image>, centerX: Int, centerY: Int, radius: Int, color: CValue<Color>) = raylib.interop.ImageDrawCircle(imagePtr, centerX, centerY, radius, color)
    override fun imageDrawCircleV(imagePtr: CPointer<Image>, center: CValue<Vector2>, radius: Int, color: CValue<Color>) = raylib.interop.ImageDrawCircleV(imagePtr, center, radius, color)
    override fun imageDrawCircleLines(imagePtr: CPointer<Image>, centerX: Int, centerY: Int, radius: Int, color: CValue<Color>) = raylib.interop.ImageDrawCircleLines(imagePtr, centerX, centerY, radius, color)
    override fun imageDrawCircleLinesV(imagePtr: CPointer<Image>, center: CValue<Vector2>, radius: Int, color: CValue<Color>) = raylib.interop.ImageDrawCircleLinesV(imagePtr, center, radius, color)

    override fun imageDrawRectangle(imagePtr: CPointer<Image>, posX: Int, posY: Int, width: Int, height: Int, color: CValue<Color>) = raylib.interop.ImageDrawRectangle(imagePtr, posX, posY, width, height, color)
    override fun imageDrawRectangleV(imagePtr: CPointer<Image>, position: CValue<Vector2>, size: CValue<Vector2>, color: CValue<Color>) = raylib.interop.ImageDrawRectangleV(imagePtr, position, size, color)
    override fun imageDrawRectangleRec(imagePtr: CPointer<Image>, rec: CValue<Rectangle>, color: CValue<Color>) = raylib.interop.ImageDrawRectangleRec(imagePtr, rec, color)
    override fun imageDrawRectangleLines(imagePtr: CPointer<Image>, rec: CValue<Rectangle>, thick: Int, color: CValue<Color>) = raylib.interop.ImageDrawRectangleLines(imagePtr, rec, thick, color)

    override fun imageDrawTriangle(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>) = raylib.interop.ImageDrawTriangle(imagePtr, v1, v2, v3, color)
    override fun imageDrawTriangleEx(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, c1: CValue<Color>, c2: CValue<Color>, c3: CValue<Color>) = raylib.interop.ImageDrawTriangleEx(imagePtr, v1, v2, v3, c1, c2, c3)
    override fun imageDrawTriangleLines(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>) = raylib.interop.ImageDrawTriangleLines(imagePtr, v1, v2, v3, color)
    override fun imageDrawTriangleFan(imagePtr: CPointer<Image>, points: CPointer<Vector2>, pointCount: Int, color: CValue<Color>) = raylib.interop.ImageDrawTriangleFan(imagePtr, points, pointCount, color)
    override fun imageDrawTriangleStrip(imagePtr: CPointer<Image>, points: CPointer<Vector2>, pointCount: Int, color: CValue<Color>) = raylib.interop.ImageDrawTriangleStrip(imagePtr, points, pointCount, color)

    override fun imageDraw(imagePtr: CPointer<Image>, image: CValue<Image>, srcRec: CValue<Rectangle>, dstRec: CValue<Rectangle>, tint: CValue<Color>) { raylib.interop.ImageDraw(imagePtr, image, srcRec, dstRec, tint) }
    override fun imageDrawText(imagePtr: CPointer<Image>, text: String, posX: Int, posY: Int, fontSize: Int, color: CValue<Color>) = raylib.interop.ImageDrawText(imagePtr, text, posX, posY, fontSize, color)
    override fun imageDrawTextEx(imagePtr: CPointer<Image>, font: CValue<Font>, text: String, position: CValue<Vector2>, fontSize: Float, spacing: Float, tint: CValue<Color>) = raylib.interop.ImageDrawTextEx(imagePtr, font, text, position, fontSize, spacing, tint)
}