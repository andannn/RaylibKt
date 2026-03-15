/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TiledText(
    /** Whether to use a bold font (default: false) */
    @SerialName("bold") val bold: Boolean = false,

    /** Hex-formatted color (#RRGGBB or #AARRGGBB) (default: #000000) */
    @SerialName("color") val color: String = "#000000",

    /** Font family (default: sans-serif) */
    @SerialName("fontfamily") val fontFamily: String = "sans-serif",

    /** Horizontal alignment (center, right, justify or left (default)) */
    @SerialName("halign") val halign: String = "left",

    /** Whether to use an italic font (default: false) */
    @SerialName("italic") val italic: Boolean = false,

    /** Whether to use kerning when placing characters (default: true) */
    @SerialName("kerning") val kerning: Boolean = true,

    /** Pixel size of font (default: 16) */
    @SerialName("pixelsize") val pixelSize: Int = 16,

    /** Whether to strike out the text (default: false) */
    @SerialName("strikeout") val strikeout: Boolean = false,

    /** The actual text string */
    @SerialName("text") val text: String = "",

    /** Whether to underline the text (default: false) */
    @SerialName("underline") val underline: Boolean = false,

    /** Vertical alignment (center, bottom or top (default)) */
    @SerialName("valign") val valign: String = "top",

    /** Whether the text is wrapped within the object bounds (default: false) */
    @SerialName("wrap") val wrap: Boolean = false
)