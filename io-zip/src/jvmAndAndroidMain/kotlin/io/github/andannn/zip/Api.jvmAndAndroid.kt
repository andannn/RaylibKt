package io.github.andannn.zip

import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.Sink
import kotlinx.io.Source

actual fun Source.deflateSource(): RawSource =
    TODO()

actual fun Sink.deflateSink(level: Int): RawSink =
    TODO()

actual fun Source.zlibSource(): RawSource =
    TODO()

actual fun Sink.zlibSink(level: Int): RawSink =
    TODO()

actual fun Source.gzipSource(): RawSource =
    TODO()

actual fun Sink.gzipSink(level: Int): RawSink =
    TODO()

internal actual val DEFAULT_COMPRESSION: Int =
    TODO()