/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents

inline fun rlMatrix(block: RlMatrixFunction.() -> Unit) {
    raylib.interop.rlPushMatrix()
    block(RlMatrix)
    raylib.interop.rlPopMatrix()
}

interface RlMatrixFunction {
    fun translate(x: Float = 0f, y: Float = 0f, z: Float = 0f)
    fun scale(x: Float = 1f, y: Float = 1f, z: Float = 1f)
    fun rotate(degree: Float, x: Float = 0f, y: Float = 0f, z: Float = 0f)
    fun multMatrix(m: CValue<Matrix>)
}

object RlMatrix : RlMatrixFunction {
    override fun translate(x: Float, y: Float, z: Float) {
        raylib.interop.rlTranslatef(x, y, z)
    }

    override fun scale(x: Float, y: Float, z: Float) {
        raylib.interop.rlScalef(x, y, z)
    }

    override fun rotate(degree: Float, x: Float, y: Float, z: Float) {
        raylib.interop.rlRotatef(angle = degree, x = x, y = y, z = z)
    }

    override fun multMatrix(m: CValue<Matrix>) {
        raylib.interop.MatrixToFloatV(m).useContents {
            raylib.interop.rlMultMatrixf(v)
        }
    }
}

