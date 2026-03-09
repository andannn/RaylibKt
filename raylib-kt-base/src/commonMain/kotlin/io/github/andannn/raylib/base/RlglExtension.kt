/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import raylib.interop.MatrixToFloatV
import raylib.interop.rlMultMatrixf
import raylib.interop.rlPopMatrix
import raylib.interop.rlPushMatrix
import raylib.interop.rlRotatef
import raylib.interop.rlScalef
import raylib.interop.rlTranslatef

inline fun rlMatrix(block: RlMatrixFunction.() -> Unit) {
    rlPushMatrix()
    block(RlMatrix)
    rlPopMatrix()
}

interface RlMatrixFunction {
    fun translate(x: Float = 0f, y: Float = 0f, z: Float = 0f)
    fun scale(x: Float = 1f, y: Float = 1f, z: Float = 1f)
    fun rotate(degree: Float, x: Float = 0f, y: Float = 0f, z: Float = 0f)
    fun multMatrix(m: CValue<Matrix>)
}

object RlMatrix : RlMatrixFunction {
    override fun translate(x: Float, y: Float, z: Float) {
        rlTranslatef(x, y, z)
    }

    override fun scale(x: Float, y: Float, z: Float) {
        rlScalef(x, y, z)
    }

    override fun rotate(degree: Float, x: Float, y: Float, z: Float) {
        rlRotatef(angle = degree, x = x, y = y, z = z)
    }

    override fun multMatrix(m: CValue<Matrix>) {
        MatrixToFloatV(m).useContents {
            rlMultMatrixf(v)
        }
    }
}

