/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.ContextProvider
import io.github.andannn.raylib.runtime.find
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents

inline fun <reified R> ContextProvider.useImage(
    fileName: String,
    crossinline convert: (CPointer<Image>) -> Unit = {},
    crossinline block: (CValue<Image>) -> R
): R {
    val windowContext = find<WindowContext>()
    val image = windowContext.loadImage(fileName)
    val convertedImage = image.useContents {
        val temp = this
        convert(temp.ptr)
        temp.readValue()
    }
    return try {
        block(convertedImage)
    } finally {
        windowContext.unLoadImage(convertedImage)
    }
}