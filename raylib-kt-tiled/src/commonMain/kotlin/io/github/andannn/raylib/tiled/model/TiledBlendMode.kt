/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TiledBlendMode {
    @SerialName("normal")
    NORMAL,
    @SerialName("add")
    ADD,
    @SerialName("multiply")
    MULTIPLY,
    @SerialName("screen")
    SCREEN,
    @SerialName("overlay")
    OVERLAY,
    @SerialName("darken")
    DARKEN,
    @SerialName("lighten")
    LIGHTEN,
    @SerialName("color-dodge")
    COLOR_DODGE,
    @SerialName("color-burn")
    COLOR_BURN,
    @SerialName("hard-light")
    HARD_LIGHT,
    @SerialName("soft-light")
    SOFT_LIGHT,
    @SerialName("difference")
    DIFFERENCE,
    @SerialName("exclusion")
    EXCLUSION
}