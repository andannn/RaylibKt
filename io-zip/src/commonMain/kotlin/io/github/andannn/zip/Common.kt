/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.zip

import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.Sink
import kotlinx.io.Source
import platform.zlib.Z_DEFAULT_COMPRESSION

/**
 * Returns a [RawSource] that decompresses this [Source] using the raw DEFLATE algorithm.
 */
fun Source.deflateSource(): RawSource =
    InflaterSource(windowBits = WindowBits.RAW_DEFLATE, source = this)

/**
 * Returns a [RawSink] that compresses data to this [Sink] using the raw DEFLATE algorithm.
 */
fun Sink.deflateSink(level: Int = Z_DEFAULT_COMPRESSION): RawSink =
    DeflaterSink(windowBits = WindowBits.RAW_DEFLATE, level = level, sink = this)

/**
 * Returns a [RawSource] that zlib-decompresses this [Source].
 */
fun Source.zlibSource(): RawSource =
    InflaterSource(windowBits = WindowBits.ZLIB, source = this)

/**
 * Returns a [RawSink] that zlib-compresses data to this [Sink].
 */
fun Sink.zlibSink(level: Int = Z_DEFAULT_COMPRESSION): RawSink =
    DeflaterSink(windowBits = WindowBits.ZLIB, level = level, sink = this)

/**
 * Returns a [RawSource] that gzip-decompresses this [Source].
 */
fun Source.gzipSource(): RawSource =
    InflaterSource(windowBits = WindowBits.GZIP, source = this)

/**
 * Returns a [RawSink] that gzip-compresses data to this [Sink].
 */
fun Sink.gzipSink(level: Int = Z_DEFAULT_COMPRESSION): RawSink =
    DeflaterSink(windowBits = WindowBits.GZIP, level = level, sink = this)

internal const val CHUNK_SIZE = 8192

internal object WindowBits {
    const val RAW_DEFLATE = -15
    const val ZLIB = 15
    const val GZIP = 31
}
