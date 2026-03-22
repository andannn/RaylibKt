/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.ContextProvider
import io.github.andannn.raylib.runtime.find
import kotlinx.cinterop.CValue

inline fun ContextProvider.useFont(
    fileName: String,
    crossinline block: (CValue<Font>) -> Unit
) {
    val windowContext = find<WindowContext>()
    val font = windowContext.loadFont(fileName)

    try {
        block(font)
    } finally {
        windowContext.unloadFont(font)
    }
}