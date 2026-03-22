/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ObjectAlignment {
    @SerialName("unspecified")
    UNSPECIFIED,

    @SerialName("topleft")
    TOP_LEFT,

    @SerialName("top")
    TOP,

    @SerialName("topright")
    TOP_RIGHT,

    @SerialName("left")
    LEFT,

    @SerialName("center")
    CENTER,

    @SerialName("right")
    RIGHT,

    @SerialName("bottomleft")
    BOTTOM_LEFT,

    @SerialName("bottom")
    BOTTOM,

    @SerialName("bottomright")
    BOTTOM_RIGHT;
}