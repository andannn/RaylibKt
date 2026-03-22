/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.FloatVar
import raylib.interop.ImageAlphaClear
import raylib.interop.ImageAlphaCrop
import raylib.interop.ImageAlphaMask
import raylib.interop.ImageAlphaPremultiply
import raylib.interop.ImageBlurGaussian
import raylib.interop.ImageClearBackground
import raylib.interop.ImageColorBrightness
import raylib.interop.ImageColorContrast
import raylib.interop.ImageColorGrayscale
import raylib.interop.ImageColorInvert
import raylib.interop.ImageColorReplace
import raylib.interop.ImageColorTint
import raylib.interop.ImageCrop
import raylib.interop.ImageDither
import raylib.interop.ImageDraw
import raylib.interop.ImageDrawCircle
import raylib.interop.ImageDrawCircleLines
import raylib.interop.ImageDrawCircleLinesV
import raylib.interop.ImageDrawCircleV
import raylib.interop.ImageDrawLine
import raylib.interop.ImageDrawLineEx
import raylib.interop.ImageDrawLineV
import raylib.interop.ImageDrawPixel
import raylib.interop.ImageDrawPixelV
import raylib.interop.ImageDrawRectangle
import raylib.interop.ImageDrawRectangleLines
import raylib.interop.ImageDrawRectangleRec
import raylib.interop.ImageDrawRectangleV
import raylib.interop.ImageDrawText
import raylib.interop.ImageDrawTextEx
import raylib.interop.ImageDrawTriangle
import raylib.interop.ImageDrawTriangleEx
import raylib.interop.ImageDrawTriangleFan
import raylib.interop.ImageDrawTriangleLines
import raylib.interop.ImageDrawTriangleStrip
import raylib.interop.ImageFlipHorizontal
import raylib.interop.ImageFlipVertical
import raylib.interop.ImageKernelConvolution
import raylib.interop.ImageMipmaps
import raylib.interop.ImageResize
import raylib.interop.ImageResizeCanvas
import raylib.interop.ImageResizeNN
import raylib.interop.ImageRotate
import raylib.interop.ImageRotateCCW
import raylib.interop.ImageRotateCW
import raylib.interop.LoadImage
import raylib.interop.UnloadImage

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
        return LoadImage(fileName)
    }

    override fun unLoadImage(image: CValue<Image>) {
        UnloadImage(image)
    }

    override fun imageCrop(imagePtr: CPointer<Image>, crop: CValue<Rectangle>) {
        ImageCrop(imagePtr, crop)
    }

    override fun imageAlphaCrop(imagePtr: CPointer<Image>, threshold: Float) {
        ImageAlphaCrop(imagePtr, threshold)
    }

    override fun imageAlphaClear(imagePtr: CPointer<Image>, color: CValue<Color>, threshold: Float) {
        ImageAlphaClear(imagePtr, color, threshold)
    }

    override fun imageAlphaMask(imagePtr: CPointer<Image>, alphaMask: CValue<Image>) {
        ImageAlphaMask(imagePtr, alphaMask)
    }

    override fun imageAlphaPremultiply(imagePtr: CPointer<Image>) {
        ImageAlphaPremultiply(imagePtr)
    }

    override fun imageBlurGaussian(imagePtr: CPointer<Image>, blurSize: Int) {
        ImageBlurGaussian(imagePtr, blurSize)
    }

    override fun imageKernelConvolution(imagePtr: CPointer<Image>, kernel: CPointer<FloatVar>, kernelSize: Int) {
        ImageKernelConvolution(imagePtr, kernel, kernelSize)
    }

    override fun imageResize(imagePtr: CPointer<Image>, newWidth: Int, newHeight: Int) {
        ImageResize(imagePtr, newWidth, newHeight)
    }

    override fun imageResizeNN(imagePtr: CPointer<Image>, newWidth: Int, newHeight: Int) {
        ImageResizeNN(imagePtr, newWidth, newHeight)
    }

    override fun imageResizeCanvas(
        imagePtr: CPointer<Image>,
        newWidth: Int,
        newHeight: Int,
        offsetX: Int,
        offsetY: Int,
        fill: CValue<Color>
    ) {
        ImageResizeCanvas(imagePtr, newWidth, newHeight, offsetX, offsetY, fill)
    }

    override fun imageMipmaps(imagePtr: CPointer<Image>) {
        ImageMipmaps(imagePtr)
    }

    override fun imageDither(imagePtr: CPointer<Image>, rBpp: Int, gBpp: Int, bBpp: Int, aBpp: Int) {
        ImageDither(imagePtr, rBpp, gBpp, bBpp, aBpp)
    }

    override fun imageFlipVertical(imagePtr: CPointer<Image>) {
        ImageFlipVertical(imagePtr)
    }

    override fun imageFlipHorizontal(imagePtr: CPointer<Image>) {
        ImageFlipHorizontal(imagePtr)
    }

    override fun imageRotate(imagePtr: CPointer<Image>, degrees: Int) {
        ImageRotate(imagePtr, degrees)
    }

    override fun imageRotateCW(imagePtr: CPointer<Image>) {
        ImageRotateCW(imagePtr)
    }

    override fun imageRotateCCW(imagePtr: CPointer<Image>) {
        ImageRotateCCW(imagePtr)
    }

    override fun imageColorTint(imagePtr: CPointer<Image>, color: CValue<Color>) {
        ImageColorTint(imagePtr, color)
    }

    override fun imageColorInvert(imagePtr: CPointer<Image>) {
        ImageColorInvert(imagePtr)
    }

    override fun imageColorGrayscale(imagePtr: CPointer<Image>) {
        ImageColorGrayscale(imagePtr)
    }

    override fun imageColorContrast(imagePtr: CPointer<Image>, contrast: Float) {
        ImageColorContrast(imagePtr, contrast)
    }

    override fun imageColorBrightness(imagePtr: CPointer<Image>, brightness: Int) {
        ImageColorBrightness(imagePtr, brightness)
    }

    override fun imageColorReplace(imagePtr: CPointer<Image>, color: CValue<Color>, replace: CValue<Color>) {
        ImageColorReplace(imagePtr, color, replace)
    }


    override fun imageClearBackground(imagePtr: CPointer<Image>, color: CValue<Color>) =
        ImageClearBackground(imagePtr, color)
    override fun imageDrawPixel(imagePtr: CPointer<Image>, posX: Int, posY: Int, color: CValue<Color>) =
        ImageDrawPixel(imagePtr, posX, posY, color)
    override fun imageDrawPixelV(imagePtr: CPointer<Image>, position: CValue<Vector2>, color: CValue<Color>) =
        ImageDrawPixelV(imagePtr, position, color)

    override fun imageDrawLine(imagePtr: CPointer<Image>, startPosX: Int, startPosY: Int, endPosX: Int, endPosY: Int, color: CValue<Color>) =
        ImageDrawLine(imagePtr, startPosX, startPosY, endPosX, endPosY, color)
    override fun imageDrawLineV(imagePtr: CPointer<Image>, start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>) =
        ImageDrawLineV(imagePtr, start, end, color)
    override fun imageDrawLineEx(imagePtr: CPointer<Image>, start: CValue<Vector2>, end: CValue<Vector2>, thick: Int, color: CValue<Color>) =
        ImageDrawLineEx(imagePtr, start, end, thick, color)

    override fun imageDrawCircle(imagePtr: CPointer<Image>, centerX: Int, centerY: Int, radius: Int, color: CValue<Color>) =
        ImageDrawCircle(imagePtr, centerX, centerY, radius, color)
    override fun imageDrawCircleV(imagePtr: CPointer<Image>, center: CValue<Vector2>, radius: Int, color: CValue<Color>) =
        ImageDrawCircleV(imagePtr, center, radius, color)
    override fun imageDrawCircleLines(imagePtr: CPointer<Image>, centerX: Int, centerY: Int, radius: Int, color: CValue<Color>) =
        ImageDrawCircleLines(imagePtr, centerX, centerY, radius, color)
    override fun imageDrawCircleLinesV(imagePtr: CPointer<Image>, center: CValue<Vector2>, radius: Int, color: CValue<Color>) =
        ImageDrawCircleLinesV(imagePtr, center, radius, color)

    override fun imageDrawRectangle(imagePtr: CPointer<Image>, posX: Int, posY: Int, width: Int, height: Int, color: CValue<Color>) =
        ImageDrawRectangle(imagePtr, posX, posY, width, height, color)
    override fun imageDrawRectangleV(imagePtr: CPointer<Image>, position: CValue<Vector2>, size: CValue<Vector2>, color: CValue<Color>) =
        ImageDrawRectangleV(imagePtr, position, size, color)
    override fun imageDrawRectangleRec(imagePtr: CPointer<Image>, rec: CValue<Rectangle>, color: CValue<Color>) =
        ImageDrawRectangleRec(imagePtr, rec, color)
    override fun imageDrawRectangleLines(imagePtr: CPointer<Image>, rec: CValue<Rectangle>, thick: Int, color: CValue<Color>) =
        ImageDrawRectangleLines(imagePtr, rec, thick, color)

    override fun imageDrawTriangle(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>) =
        ImageDrawTriangle(imagePtr, v1, v2, v3, color)
    override fun imageDrawTriangleEx(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, c1: CValue<Color>, c2: CValue<Color>, c3: CValue<Color>) =
        ImageDrawTriangleEx(imagePtr, v1, v2, v3, c1, c2, c3)
    override fun imageDrawTriangleLines(imagePtr: CPointer<Image>, v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>) =
        ImageDrawTriangleLines(imagePtr, v1, v2, v3, color)
    override fun imageDrawTriangleFan(imagePtr: CPointer<Image>, points: CPointer<Vector2>, pointCount: Int, color: CValue<Color>) =
        ImageDrawTriangleFan(imagePtr, points, pointCount, color)
    override fun imageDrawTriangleStrip(imagePtr: CPointer<Image>, points: CPointer<Vector2>, pointCount: Int, color: CValue<Color>) =
        ImageDrawTriangleStrip(imagePtr, points, pointCount, color)

    override fun imageDraw(imagePtr: CPointer<Image>, image: CValue<Image>, srcRec: CValue<Rectangle>, dstRec: CValue<Rectangle>, tint: CValue<Color>) {
        ImageDraw(imagePtr, image, srcRec, dstRec, tint)
    }
    override fun imageDrawText(imagePtr: CPointer<Image>, text: String, posX: Int, posY: Int, fontSize: Int, color: CValue<Color>) =
        ImageDrawText(imagePtr, text, posX, posY, fontSize, color)
    override fun imageDrawTextEx(imagePtr: CPointer<Image>, font: CValue<Font>, text: String, position: CValue<Vector2>, fontSize: Float, spacing: Float, tint: CValue<Color>) =
        ImageDrawTextEx(imagePtr, font, text, position, fontSize, spacing, tint)
}