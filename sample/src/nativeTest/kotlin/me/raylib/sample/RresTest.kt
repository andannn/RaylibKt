package me.raylib.sample

import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.io.readIntLe
import kotlinx.io.readShortLe
import kotlinx.io.readUInt
import kotlinx.io.readUIntLe
import kotlin.test.Test
import kotlin.test.assertEquals

class RresTest {
    @Test
    fun test() {
        val file = SystemFileSystem.source(Path("resources/resources.rres")).buffered()

        // rresFileHeader

        // Signature Id
        assertEquals('r', file.readByte().toInt().toChar())
        assertEquals('r', file.readByte().toInt().toChar())
        assertEquals('e', file.readByte().toInt().toChar())
        assertEquals('s', file.readByte().toInt().toChar())

        // Version
        assertEquals(100, file.readShortLe())
        // Resource Count
        val chunkCount = file.readShortLe().also {
            assertEquals(22, it)
        }
        // CD Offset
        assertEquals(11204406, file.readIntLe())
//        file.skip(922200576)

        // Reserved
        assertEquals(0, file.readIntLe())

        // [rresResourceChunk]
        file.chunk()

        repeat(chunkCount.toInt()) { i ->
            file.chunk()
        }
    }
}

private fun Source.chunk() {
    println("chunk begin ------------------------------------------------------------")
    // rresResourceChunkInfo
    // Resource chunk type (FourCC)
    println("Resource chunk type (FourCC)")
    println(readByte().toInt().toChar())
    println(readByte().toInt().toChar())
    println(readByte().toInt().toChar())
    println(readByte().toInt().toChar())

    // Resource chunk identifier (generated from filename CRC32 hash)
    println("Resource chunk identifier (generated from filename CRC32 hash)")
    println(readUIntLe())
    // Data compression algorithm
    println("Data compression algorithm")
    val compressType = readByte().also {
        println(it)
    }
    // Data encription algorithm
    println("Data encription algorithm")
    val encriptionType = readByte().also {
        println(it)
    }
    // Data flags (if required)
    println("Data flags (if required)")
    println(readShortLe())
    // packedSize  Data chunk size (compressed/encrypted + custom data appended)
    println("Data chunk size (compressed/encrypted + custom data appended)")
    val packedSize = readUIntLe().also {
        println(it)
    }
    // Data base size (uncompressed/unencrypted)
    println("Data base size (uncompressed/unencrypted)")
    val baseSize = readUIntLe().also {
        println(it)
    }
    // Next resource chunk global offset (if resource has multiple chunks)
    println("Next resource chunk global offset (if resource has multiple chunks)")
    println(readUIntLe())
    // <reserved>
    println("<reserved>")
    println(readUIntLe())
    // Data chunk CRC32 (propCount + props[] + data)
    println("Data chunk CRC32 (propCount + props[] + data)")
    println(readUIntLe())

    if (compressType.toInt() == 0 && encriptionType.toInt() == 0) {
        println("No encription and compression")

        // Resource chunk properties count
        println("Resource chunk properties count")
        val propCount = readUIntLe().also {
            println(it)
        }

        // Resource chunk properties
        println("Resource chunk properties")
        repeat(propCount.toInt()) {
            println("propertie [$it]: ${readUIntLe()}")
        }

        val rawSize = baseSize.toInt() - 4 - propCount.toInt() * 4
        val raw = readByteArray(rawSize)
//        int rawSize = info.baseSize - sizeof(int) - (chunkData.propCount*sizeof(int));
//        chunkData.raw = RRES_CALLOC(rawSize, 1);
//        if (chunkData.raw != NULL) memcpy(chunkData.raw, ((unsigned char *)data) + sizeof(int) + (chunkData.propCount*sizeof(int)), rawSize);
    } else {
        // Data is compressed/encrypted
        // Just return the loaded resource packed data from .rres file,
        // it's up to the user to manage decompression/decryption on user library
        println("data is compressed/encrypted")
        val raw = readByteArray(packedSize.toInt())
    }
    println("chunk end   ------------------------------------------------------------")
}