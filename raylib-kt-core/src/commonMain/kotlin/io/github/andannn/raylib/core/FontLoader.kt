/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.Font
import kotlinx.cinterop.CValue


inline fun RememberScope.useFont(
    fileName: String,
    crossinline block: (CValue<Font>) -> Unit
) {
    val font = loadFont(fileName)

    try {
        block(font)
    } finally {
        unloadFont(font)
    }
}