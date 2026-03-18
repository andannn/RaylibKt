/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.rres

import io.github.andannn.raylib.base.Font
import io.github.andannn.raylib.base.Image
import io.github.andannn.raylib.base.Mesh
import io.github.andannn.raylib.base.Wave
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.CValue
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readValue
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

interface ResourceFunction : RresFunction, RaylibRresFunction

fun ResourceFunction(
    rresFunction: RresFunction = RresFunction(),
    raylibRresFunction: RaylibRresFunction = RaylibRresFunction(),
): ResourceFunction = object : ResourceFunction,
    RresFunction by rresFunction,
    RaylibRresFunction by raylibRresFunction {}

interface RresFunction {
    fun loadResourceChunk(fileName: String, rresId: UInt): CValue<RresResourceChunk>
    fun unloadResourceChunk(chunk: CValue<RresResourceChunk>)

    fun loadResourceMulti(fileName: String, rresId: UInt): CValue<RresResourceMulti>
    fun unloadResourceMulti(multi: CValue<RresResourceMulti>)

    fun loadResourceChunkInfo(fileName: String, rresId: UInt): CValue<RresResourceChunkInfo>
    fun loadResourceChunkInfoAll(fileName: String, chunkCount: CPointer<UIntVar>): CPointer<RresResourceChunkInfo>?

    fun loadCentralDirectory(fileName: String): CValue<RresCentralDir>
    fun unloadCentralDirectory(dir: CValue<RresCentralDir>)

    fun getDataType(fourCC: CPointer<UByteVar>): UInt
    fun getResourceId(dir: CValue<RresCentralDir>, fileName: String): UInt
    fun computeCRC32(data: CPointer<UByteVar>, len: Int): UInt

    fun setCipherPassword(pass: String?)
    fun getCipherPassword(): String?
}

private fun RresFunction(): RresFunction = DefaultRresFunction()

private class DefaultRresFunction : RresFunction {
    override fun loadResourceChunk(fileName: String, rresId: UInt): CValue<RresResourceChunk> {
        return rres.interop.rresLoadResourceChunk(fileName, rresId)
    }

    override fun unloadResourceChunk(chunk: CValue<RresResourceChunk>) {
        rres.interop.rresUnloadResourceChunk(chunk)
    }

    override fun loadResourceMulti(fileName: String, rresId: UInt): CValue<RresResourceMulti> {
        return rres.interop.rresLoadResourceMulti(fileName, rresId)
    }

    override fun unloadResourceMulti(multi: CValue<RresResourceMulti>) {
        rres.interop.rresUnloadResourceMulti(multi)
    }

    override fun loadResourceChunkInfo(fileName: String, rresId: UInt): CValue<RresResourceChunkInfo> {
        return rres.interop.rresLoadResourceChunkInfo(fileName, rresId)
    }

    override fun loadResourceChunkInfoAll(
        fileName: String,
        chunkCount: CPointer<UIntVar>
    ): CPointer<RresResourceChunkInfo>? {
        return rres.interop.rresLoadResourceChunkInfoAll(fileName, chunkCount)
    }

    override fun loadCentralDirectory(fileName: String): CValue<RresCentralDir> {
        return rres.interop.rresLoadCentralDirectory(fileName)
    }

    override fun unloadCentralDirectory(dir: CValue<RresCentralDir>) {
        rres.interop.rresUnloadCentralDirectory(dir)
    }

    override fun getDataType(fourCC: CPointer<UByteVar>): UInt {
        return rres.interop.rresGetDataType(fourCC)
    }

    override fun getResourceId(dir: CValue<RresCentralDir>, fileName: String): UInt {
        return rres.interop.rresGetResourceId(dir, fileName)
    }

    override fun computeCRC32(data: CPointer<UByteVar>, len: Int): UInt {
        return rres.interop.rresComputeCRC32(data, len)
    }

    override fun setCipherPassword(pass: String?) {
        rres.interop.rresSetCipherPassword(pass)
    }

    override fun getCipherPassword(): String? {
        return rres.interop.rresGetCipherPassword()?.toKString()
    }
}

interface RaylibRresFunction {
    fun loadDataFromResource(chunk: CValue<RresResourceChunk>, size: CPointer<UIntVar>): COpaquePointer?
    fun loadTextFromResource(chunk: CValue<RresResourceChunk>): CPointer<ByteVar>?

    fun loadImageFromResource(chunk: CValue<RresResourceChunk>): CValue<Image>
    fun loadWaveFromResource(chunk: CValue<RresResourceChunk>): CValue<Wave>
    fun loadFontFromResource(multi: CValue<RresResourceMulti>): CValue<Font>
    fun loadMeshFromResource(multi: CValue<RresResourceMulti>): CValue<Mesh>

    fun unpackResourceChunk(chunk: CPointer<RresResourceChunk>): Int
    fun setBaseDirectory(baseDir: String)
}

fun RaylibRresFunction(): RaylibRresFunction = DefaultRaylibRresFunction()

private class DefaultRaylibRresFunction : RaylibRresFunction {

    override fun loadDataFromResource(chunk: CValue<RresResourceChunk>, size: CPointer<UIntVar>): COpaquePointer? {
        return rres.interop.LoadDataFromResource(chunk, size)
    }

    override fun loadTextFromResource(chunk: CValue<RresResourceChunk>): CPointer<ByteVar>? {
        return rres.interop.LoadTextFromResource(chunk)
    }

    override fun loadImageFromResource(chunk: CValue<RresResourceChunk>): CValue<Image> {
        return rres.interop.LoadImageFromResource(chunk).reinterpret()
    }

    override fun loadWaveFromResource(chunk: CValue<RresResourceChunk>): CValue<Wave> {
        return rres.interop.LoadWaveFromResource(chunk).reinterpret()
    }

    override fun loadFontFromResource(multi: CValue<RresResourceMulti>): CValue<Font> {
        return rres.interop.LoadFontFromResource(multi).reinterpret()
    }

    override fun loadMeshFromResource(multi: CValue<RresResourceMulti>): CValue<Mesh> {
        return rres.interop.LoadMeshFromResource(multi).reinterpret()
    }

    override fun unpackResourceChunk(chunk: CPointer<RresResourceChunk>): Int {
        return rres.interop.UnpackResourceChunk(chunk)
    }

    override fun setBaseDirectory(baseDir: String) {
        rres.interop.SetBaseDirectory(baseDir)
    }
}

inline fun <reified S : CStructVar, reified T : CStructVar> CValue<S>.reinterpret(): CValue<T> = memScoped {
    val from = this@reinterpret.getPointer(this)
    val to: CPointer<T> = from.reinterpret()
    to.pointed.readValue()
}
