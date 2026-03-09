/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.Image
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents

inline fun <reified R> RememberScope.useImage(
    fileName: String,
    crossinline convert: (CPointer<Image>) -> Unit = {},
    crossinline block: (CValue<Image>) -> R
): R {
    val image = loadImage(fileName)
    val convertedImage = image.useContents {
        val temp = this
        convert(temp.ptr)
        temp.readValue()
    }
    return try {
        block(convertedImage)
    } finally {
        unLoadImage(convertedImage)
    }
}