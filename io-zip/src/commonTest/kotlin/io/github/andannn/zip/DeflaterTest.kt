/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.zip

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.io.writeString
import kotlin.io.encoding.Base64
import kotlin.test.Test
import kotlin.test.assertEquals

class DeflaterTest {
    @Test
    fun deflaterTest_compress_wrap() {
        val content = "Hello, Kotlin Multiplatform! Raw Deflate is awesome."
        val target = Buffer()
        val source = Buffer().apply { writeString(content) }
        val deflater = DeflaterSink(8, windowBits = WindowBits.ZLIB, target)
        deflater.write(source, source.size)
        deflater.close()
        assertEquals(
            "eNrzSM3JyddR8M4vycnMU/AtzSnJLMhJLEnLL8pVVAhKLFdwSU0D8lMVMosVEstTi/NzU/UA8K4SvQ==",
            Base64.encode(target.readByteArray())
        )
    }

    @Test
    fun deflaterTest_compress_no_wrap() {
        val content = "Hello, Kotlin Multiplatform! Raw Deflate is awesome."
        val target = Buffer()
        val source = Buffer().apply { writeString(content) }
        val deflater = DeflaterSink(8, windowBits = WindowBits.RAW_DEFLATE, target)
        deflater.write(source, source.size)
        deflater.close()
        assertEquals(
            "80jNycnXUfDOL8nJzFPwLc0pySzISSxJyy/KVVQISixXcElNA/JTFTKLFRLLU4vzc1P1AA==",
            Base64.encode(target.readByteArray())
        )
    }
}