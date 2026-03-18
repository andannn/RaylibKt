/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.model

value class GID(val rawGid: UInt) {

    val tileId: Int
        get() = (rawGid and MASK.inv()).toInt()

    val hasFlips: Boolean
        get() = (rawGid and MASK) != 0u

    val flipHorizontal: Boolean get() = (rawGid and FLIPPED_HORIZONTALLY_FLAG) != 0u
    val flipVertical: Boolean get() = (rawGid and FLIPPED_VERTICALLY_FLAG) != 0u
    val flipDiagonal: Boolean get() = (rawGid and FLIPPED_DIAGONALLY_FLAG) != 0u


    override fun toString(): String {
        return "[GID: $tileId, flipHorizontal=$flipHorizontal, flipVertical=$flipVertical, flipDiagonal=$flipDiagonal]"

    }

    private companion object {
        private const val FLIPPED_HORIZONTALLY_FLAG = 0x80000000u
        private const val FLIPPED_VERTICALLY_FLAG = 0x40000000u
        private const val FLIPPED_DIAGONALLY_FLAG = 0x20000000u
        private const val FLIPPED_ANTI_DIAGONALLY_FLAG = 0x10000000u

        private val MASK = FLIPPED_HORIZONTALLY_FLAG or
                FLIPPED_VERTICALLY_FLAG or
                FLIPPED_DIAGONALLY_FLAG or
                FLIPPED_ANTI_DIAGONALLY_FLAG
    }
}
