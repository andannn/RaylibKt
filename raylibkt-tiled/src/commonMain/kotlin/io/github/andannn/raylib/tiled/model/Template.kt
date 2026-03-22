/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Template(
    @SerialName("object")
    val obj: Object,
    @SerialName("tileset")
    val tileset: Tileset? = null,
    @SerialName("type")
    val type: String
)