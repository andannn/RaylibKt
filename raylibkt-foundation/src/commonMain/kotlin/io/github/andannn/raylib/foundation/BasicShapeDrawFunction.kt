/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import kotlinx.cinterop.CValue
import raylib.interop.DrawCircleGradient
import raylib.interop.DrawCircleLines
import raylib.interop.DrawCircleLinesV
import raylib.interop.DrawCircleSectorLines
import raylib.interop.DrawCircleV
import raylib.interop.DrawEllipse
import raylib.interop.DrawEllipseLines
import raylib.interop.DrawGrid
import raylib.interop.DrawLine
import raylib.interop.DrawLineBezier
import raylib.interop.DrawLineV
import raylib.interop.DrawPoly
import raylib.interop.DrawPolyLines
import raylib.interop.DrawPolyLinesEx
import raylib.interop.DrawRectangle
import raylib.interop.DrawRectangleGradientH
import raylib.interop.DrawRectangleLines
import raylib.interop.DrawRectangleLinesEx
import raylib.interop.DrawRectanglePro
import raylib.interop.DrawRectangleRec
import raylib.interop.DrawRectangleV
import raylib.interop.DrawRing
import raylib.interop.DrawRingLines
import raylib.interop.DrawTriangle
import raylib.interop.DrawTriangleLines

interface BasicShapeDrawFunction {
    fun drawCircle(centerX: Int, centerY: Int, radius: Float, color: CValue<Color>)

    fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>)

    fun drawCircleGradient(x: Int, y: Int, radius: Float, color1: CValue<Color>, color2: CValue<Color>)

    fun drawCircleLines(x: Int, y: Int, radius: Float, color: CValue<Color>)
    fun drawCircleLines(center: CValue<Vector2>, radius: Float, color: CValue<Color>)
    fun drawEllipse(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>)
    fun drawEllipseLines(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>)
    fun drawRectangle(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>)
    fun drawRectangle(
        position: CValue<raylib.interop.Vector2>,
        size: CValue<raylib.interop.Vector2>,
        color: CValue<raylib.interop.Color>
    )

    fun drawRectangle(
        rectangle: CValue<Rectangle>,
        origin: CValue<raylib.interop.Vector2>,
        rotation: Float,
        color: CValue<raylib.interop.Color>
    )

    fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>)
    fun drawRectangleGradientH(x: Int, y: Int, width: Int, height: Int, color1: CValue<Color>, color2: CValue<Color>)

    fun drawRectangleLines(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>)

    fun drawRectangleLines(rectangle: CValue<Rectangle>, lineThick: Float, color: CValue<Color>)

    fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>)

    fun drawLine(start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>)

    fun drawTriangle(v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>)

    fun drawTriangleLines(
        v1: CValue<Vector2>,
        v2: CValue<Vector2>,
        v3: CValue<Vector2>,
        color: CValue<Color>
    )

    fun drawPoly(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    )

    fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    )

    fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        lineThick: Float,
        color: CValue<Color>
    )

    fun drawLineBezier(
        start: CValue<Vector2>,
        end: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    )

    fun drawRing(
        center: CValue<Vector2>,
        innerRadius: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    )

    fun drawRingLines(
        center: CValue<Vector2>,
        innerRadius: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    )

    fun drawCircleSectorLines(
        center: CValue<raylib.interop.Vector2>,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    )

    fun drawGrid(slices: Int, spacing: Float)
}

fun BasicShapeDrawFunction(): BasicShapeDrawFunction {
    return DefaultBasicShapeDrawFunction()
}

private class DefaultBasicShapeDrawFunction() : BasicShapeDrawFunction {
    override fun drawCircleGradient(
        x: Int,
        y: Int,
        radius: Float,
        color1: CValue<Color>,
        color2: CValue<Color>
    ) {
        DrawCircleGradient(x, y, radius, color1, color2)
    }

    override fun drawCircleLines(x: Int, y: Int, radius: Float, color: CValue<Color>) {
        DrawCircleLines(x, y, radius, color)
    }

    override fun drawCircleLines(center: CValue<Vector2>, radius: Float, color: CValue<Color>) {
        DrawCircleLinesV(center, radius, color)
    }

    override fun drawEllipse(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>) {
        DrawEllipse(x, y, radiusH, radiusV, color)
    }

    override fun drawEllipseLines(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>) {
        DrawEllipseLines(x, y, radiusH, radiusV, color)
    }

    override fun drawCircle(centerX: Int, centerY: Int, radius: Float, color: CValue<Color>) {
        drawCircle(Vector2(centerX.toFloat(), centerY.toFloat()), radius, color)
    }

    override fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>) {
        DrawCircleV(center, radius, color)
    }

    override fun drawRectangle(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>) {
        DrawRectangle(x, y, width, height, color)
    }

    override fun drawRectangle(
        position: CValue<raylib.interop.Vector2>,
        size: CValue<raylib.interop.Vector2>,
        color: CValue<raylib.interop.Color>
    ) {
        DrawRectangleV(position, size, color)
    }

    override fun drawRectangle(
        rectangle: CValue<Rectangle>,
        origin: CValue<raylib.interop.Vector2>,
        rotation: Float,
        color: CValue<raylib.interop.Color>
    ) {
        DrawRectanglePro(rectangle, origin, rotation, color)
    }

    override fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>) {
        DrawRectangleRec(rec, color)
    }

    override fun drawRectangleGradientH(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color1: CValue<Color>,
        color2: CValue<Color>
    ) {
        DrawRectangleGradientH(x, y, width, height, color1, color2)
    }

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>) {
        DrawLine(x1, y1, x2, y2, color)
    }

    override fun drawLine(start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>) {
        DrawLineV(start, end, color)
    }

    override fun drawTriangle(v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>) {
        DrawTriangle(v1, v2, v3, color)
    }

    override fun drawTriangleLines(
        v1: CValue<Vector2>,
        v2: CValue<Vector2>,
        v3: CValue<Vector2>,
        color: CValue<Color>
    ) {
        DrawTriangleLines(v1, v2, v3, color)
    }

    override fun drawPoly(center: CValue<Vector2>, sides: Int, radius: Float, rotation: Float, color: CValue<Color>) {
        DrawPoly(center, sides, radius, rotation, color)
    }

    override fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    ) {
        DrawPolyLines(center, sides, radius, rotation, color)
    }

    override fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        lineThick: Float,
        color: CValue<Color>
    ) {
        DrawPolyLinesEx(center, sides, radius, rotation, lineThick, color)
    }

    override fun drawLineBezier(start: CValue<Vector2>, end: CValue<Vector2>, thick: Float, color: CValue<Color>) {
        DrawLineBezier(start, end, thick, color)
    }

    override fun drawRing(
        center: CValue<Vector2>,
        innerRadius: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    ) {
        DrawRing(center, innerRadius, outerRadius, startAngle, endAngle, segments, color)
    }

    override fun drawRingLines(
        center: CValue<Vector2>,
        innerRadius: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    ) {
        DrawRingLines(center, innerRadius, outerRadius, startAngle, endAngle, segments, color)
    }

    override fun drawCircleSectorLines(
        center: CValue<raylib.interop.Vector2>,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        segments: Int,
        color: CValue<Color>
    ) {
        DrawCircleSectorLines(center, radius, startAngle, endAngle, segments, color)
    }

    override fun drawGrid(slices: Int, spacing: Float) {
        DrawGrid(slices, spacing)
    }

    override fun drawRectangleLines(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>) {
        DrawRectangleLines(x, y, width, height, color)
    }

    override fun drawRectangleLines(
        rectangle: CValue<Rectangle>,
        lineThick: Float,
        color: CValue<Color>
    ) {
        DrawRectangleLinesEx(rectangle, lineThick, color)
    }
}
