/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.zip

import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlin.io.encoding.Base64
import kotlin.test.Test
import kotlin.test.assertEquals

class InflaterTest {
    @Test
    fun inflaterTest_wrap() {
        val expected = "Your test string, 1234567890, asdflgjqweroiutyjbioasdkljb"
        val sourceByteArray =
            Base64.decode("eJyLzC8tUihJLS5RKC4pysxL11EwNDI2MTUzt7A00FFILE5Jy0nPKixPLcrPLC2pzErKzAeKZedkJQEAMAMUPw==")
        val source = InflaterSource(windowBits = WindowBits.ZLIB, Buffer().apply { write(sourceByteArray) })
        val target = Buffer()
        source.readAtMostTo(target, Long.MAX_VALUE)
        source.close()
        assertEquals(expected, target.readString())
    }

    @Test
    fun inflaterTest_no_wrap() {
        val expected = "Hello, Kotlin Multiplatform! Raw Deflate is awesome."
        val sourceByteArray =
            Base64.decode("80jNycnXUfDOL8nJzFPwLc0pySzISSxJyy/KVVQISixXcElNA/JTFTKLFRLLU4vzc1P1AA==")
        val source = InflaterSource(windowBits = WindowBits.RAW_DEFLATE, Buffer().apply { write(sourceByteArray) })
        val target = Buffer()
        source.readAtMostTo(target, Long.MAX_VALUE)
        source.close()
        assertEquals(expected, target.readString())
    }
}