/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import raylib.interop.GetRandomValue

fun randomValue(min: Int, max: Int) = GetRandomValue(min, max)

fun randomColor(): CValue<Color> = Color(
    randomValue(0, 255),
    randomValue(0, 255),
    randomValue(0, 255),
    255
)

inline fun <reified T : CStructVar> Iterable<CValue<T>>.forEachContents(
    action: (T) -> Unit
) {
    forEach {
        it.useContents<T, Unit> {
            action(this)
        }
    }
}

inline fun <reified T : CStructVar> Iterable<CValue<T>>.forEachContentsIndexed(
    action: (index: Int, T) -> Unit
) {
    forEachIndexed { index, value ->
        value.useContents<T, Unit> {
            action(index, this)
        }
    }
}