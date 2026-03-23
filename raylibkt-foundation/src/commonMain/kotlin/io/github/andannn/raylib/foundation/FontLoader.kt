/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.DisposableRegistry
import kotlinx.cinterop.CValue

inline fun useFont(
    fileName: String,
    crossinline block: (CValue<Font>) -> Unit
) {
    val font = raylib.interop.LoadFont(fileName)
    try {
        block(font)
    } finally {
        raylib.interop.UnloadFont(font)
    }
}

fun DisposableRegistry.loadFont(
    fileName: String,
): CValue<Font> {
    val font = raylib.interop.LoadFont(fileName)
    disposeOnClose {
        raylib.interop.UnloadFont(font)
    }

    return font
}
