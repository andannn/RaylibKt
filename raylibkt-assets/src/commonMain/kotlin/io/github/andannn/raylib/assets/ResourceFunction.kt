/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.assets

import io.github.andannn.raylib.foundation.Font
import io.github.andannn.raylib.foundation.Image
import io.github.andannn.raylib.foundation.Mesh
import io.github.andannn.raylib.foundation.Wave
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

interface RresFunction {
    fun loadResourceChunk(fileName: String, rresId: UInt): CValue<ResourceChunk>
    fun unloadResourceChunk(chunk: CValue<ResourceChunk>)

    fun loadResourceMulti(fileName: String, rresId: UInt): CValue<ResourceMulti>
    fun unloadResourceMulti(multi: CValue<ResourceMulti>)

    fun loadResourceChunkInfo(fileName: String, rresId: UInt): CValue<ResourceChunkInfo>
    fun loadResourceChunkInfoAll(fileName: String, chunkCount: CPointer<UIntVar>): CPointer<ResourceChunkInfo>?

    fun loadCentralDirectory(fileName: String): CValue<CentralDir>
    fun unloadCentralDirectory(dir: CValue<CentralDir>)

    fun getDataType(fourCC: CPointer<UByteVar>): UInt
    fun getResourceId(dir: CValue<CentralDir>, fileName: String): UInt
    fun computeCRC32(data: CPointer<UByteVar>, len: Int): UInt

    fun setCipherPassword(pass: String?)
    fun getCipherPassword(): String?
}

fun RresFunction(): RresFunction = DefaultRresFunction()

private class DefaultRresFunction : RresFunction {
    override fun loadResourceChunk(fileName: String, rresId: UInt): CValue<ResourceChunk> {
        return rres.interop.rresLoadResourceChunk(fileName, rresId)
    }

    override fun unloadResourceChunk(chunk: CValue<ResourceChunk>) {
        rres.interop.rresUnloadResourceChunk(chunk)
    }

    override fun loadResourceMulti(fileName: String, rresId: UInt): CValue<ResourceMulti> {
        return rres.interop.rresLoadResourceMulti(fileName, rresId)
    }

    override fun unloadResourceMulti(multi: CValue<ResourceMulti>) {
        rres.interop.rresUnloadResourceMulti(multi)
    }

    override fun loadResourceChunkInfo(fileName: String, rresId: UInt): CValue<ResourceChunkInfo> {
        return rres.interop.rresLoadResourceChunkInfo(fileName, rresId)
    }

    override fun loadResourceChunkInfoAll(
        fileName: String,
        chunkCount: CPointer<UIntVar>
    ): CPointer<ResourceChunkInfo>? {
        return rres.interop.rresLoadResourceChunkInfoAll(fileName, chunkCount)
    }

    override fun loadCentralDirectory(fileName: String): CValue<CentralDir> {
        return rres.interop.rresLoadCentralDirectory(fileName)
    }

    override fun unloadCentralDirectory(dir: CValue<CentralDir>) {
        rres.interop.rresUnloadCentralDirectory(dir)
    }

    override fun getDataType(fourCC: CPointer<UByteVar>): UInt {
        return rres.interop.rresGetDataType(fourCC)
    }

    override fun getResourceId(dir: CValue<CentralDir>, fileName: String): UInt {
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
    fun loadDataFromResource(chunk: CValue<ResourceChunk>, size: CPointer<UIntVar>): COpaquePointer?
    fun loadTextFromResource(chunk: CValue<ResourceChunk>): CPointer<ByteVar>?

    fun loadImageFromResource(chunk: CValue<ResourceChunk>): CValue<Image>
    fun loadWaveFromResource(chunk: CValue<ResourceChunk>): CValue<Wave>
    fun loadFontFromResource(multi: CValue<ResourceMulti>): CValue<Font>
    fun loadMeshFromResource(multi: CValue<ResourceMulti>): CValue<Mesh>

    fun unpackResourceChunk(chunk: CPointer<ResourceChunk>): Int
}

fun RaylibRresFunction(): RaylibRresFunction = DefaultRaylibRresFunction()

private class DefaultRaylibRresFunction : RaylibRresFunction {

    override fun loadDataFromResource(chunk: CValue<ResourceChunk>, size: CPointer<UIntVar>): COpaquePointer? {
        return rres.interop.LoadDataFromResource(chunk, size)
    }

    override fun loadTextFromResource(chunk: CValue<ResourceChunk>): CPointer<ByteVar>? {
        return rres.interop.LoadTextFromResource(chunk)
    }

    override fun loadImageFromResource(chunk: CValue<ResourceChunk>): CValue<Image> {
        return rres.interop.LoadImageFromResource(chunk).reinterpret()
    }

    override fun loadWaveFromResource(chunk: CValue<ResourceChunk>): CValue<Wave> {
        return rres.interop.LoadWaveFromResource(chunk).reinterpret()
    }

    override fun loadFontFromResource(multi: CValue<ResourceMulti>): CValue<Font> {
        return rres.interop.LoadFontFromResource(multi).reinterpret()
    }

    override fun loadMeshFromResource(multi: CValue<ResourceMulti>): CValue<Mesh> {
        return rres.interop.LoadMeshFromResource(multi).reinterpret()
    }

    override fun unpackResourceChunk(chunk: CPointer<ResourceChunk>): Int {
        return rres.interop.UnpackResourceChunk(chunk)
    }
}

inline fun <reified S : CStructVar, reified T : CStructVar> CValue<S>.reinterpret(): CValue<T> = memScoped {
    val from = this@reinterpret.getPointer(this)
    val to: CPointer<T> = from.reinterpret()
    to.pointed.readValue()
}
