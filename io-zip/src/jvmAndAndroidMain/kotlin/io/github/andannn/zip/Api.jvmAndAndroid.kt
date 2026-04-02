package io.github.andannn.zip

import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.asInputStream
import kotlinx.io.asOutputStream
import kotlinx.io.asSink
import kotlinx.io.asSource
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream

actual fun Source.deflateSource(): RawSource =
    InflaterInputStream(asInputStream(), Inflater(true)).asSource()

actual fun Sink.deflateSink(level: Int): RawSink =
    DeflaterOutputStream(asOutputStream(), Deflater(level, true)).asSink()

actual fun Source.zlibSource(): RawSource =
    InflaterInputStream(asInputStream(), Inflater(false)).asSource()

actual fun Sink.zlibSink(level: Int): RawSink =
    DeflaterOutputStream(asOutputStream(), Deflater(level, false)).asSink()

actual fun Source.gzipSource(): RawSource =
    GZIPInputStream(asInputStream()).asSource()

// TODO: support level for jvm
actual fun Sink.gzipSink(level: Int): RawSink =
    GZIPOutputStream(asOutputStream()).asSink()

internal actual val DEFAULT_COMPRESSION: Int = Deflater.DEFAULT_COMPRESSION
