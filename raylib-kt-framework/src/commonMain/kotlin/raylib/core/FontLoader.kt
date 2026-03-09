/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core

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