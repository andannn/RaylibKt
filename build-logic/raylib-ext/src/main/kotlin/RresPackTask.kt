import com.android.utils.appendCapitalized
import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.readByteArray
import kotlinx.io.writeShortLe
import kotlinx.io.writeString
import kotlinx.io.writeUIntLe
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File
import java.io.RandomAccessFile
import java.util.zip.CRC32

abstract class RresPackTask : DefaultTask() {
    @get:InputDirectory
    abstract val baseDirectory: DirectoryProperty

    @get:Input
    abstract val fileName: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:OutputFile
    abstract val mappingFile: RegularFileProperty

    init {
        description = "Package resource to .rres"
        group = "Build"
    }

    @get:Nested
    abstract val resourceConfigs: MapProperty<String, RresResourceConfig>

    @TaskAction
    fun pack() {
        val baseDir = baseDirectory.get().asFile
        val mappingFile = mappingFile.get().asFile
        val outDirectory = outputDir.get().asFile
        outDirectory.mkdirs()
        val outFile = File(outDirectory, "${fileName.get()}.rres")
        val mappingFileSink = mappingFile.outputStream().asSink().buffered()

        println("[RresPacker] packing...")
        println("base dir: ${baseDir.absolutePath}")
        println("output: ${outFile.absolutePath}")

        val fileWithConfig = mutableMapOf<File, RresResourceConfig>()
        val configs = resourceConfigs.get()
        configs.forEach { (relativePath, config) ->
            val actualFile = baseDir.resolve(relativePath).normalize()
            if (actualFile.isDirectory) {
                actualFile.walkTopDown()
                    .filter { it.isFile }
                    .forEach { file ->
                        println("[$relativePath]: $file")
                        fileWithConfig[file] = config
                    }

            } else {
                println("[$relativePath]: $actualFile")
                fileWithConfig[actualFile] = config
            }
        }

        val sink = outFile.outputStream().asSink().buffered()

        val fileInfos = mutableListOf<ResourceInfo>()
        var offset = 0

        sink.writRresFileHeader(resourceCount = fileWithConfig.size)
        offset += 16

        fileWithConfig.forEach { (file, config) ->
            val relativePath = file.relativeTo(baseDir).path

            val chunk = buildChunk(file, config)

            val resourceId = generateResourceId(relativePath)
            fileInfos.add(ResourceInfo(resourceId, relativePath, offset))

            offset += sink.writeChunk(chunk, resourceId)
            sink.flush()

            mappingFileSink.writeString("$relativePath,$resourceId\n")
        }
        // flush and close mapping file.
        mappingFileSink.flush()
        mappingFileSink.close()
        println("resourceID mapping file is written to $mappingFile")

        // write central dir
        val centralDictOffset = offset - 16
        sink.writeChunk(Chunk.CentralDirectoryChunk(fileInfos), 0u)
        sink.flush()
        sink.close()

        RandomAccessFile(outFile, "rw").use { raf ->
            raf.seek(8)
            val littleEndianOffset = Integer.reverseBytes(centralDictOffset)
            raf.writeInt(littleEndianOffset)
        }

        println("[RresPacker] end")
    }

    private fun buildChunk(file: File, config: RresResourceConfig): Chunk {
        val source = file.inputStream().asSource()
        val rawData = source.buffered().readByteArray()
        source.close()

        return when (config) {
            is RawConfig -> {
                Chunk.RawChunk(
                    size = rawData.size,
                    data = rawData
                )
            }

            is TextConfig -> {
                Chunk.TextChunk(
                    size = rawData.size,
                    data = rawData,
                    rresTextEncoding = config.textEncoding.getOrElse(TextEncoding.RRES_TEXT_ENCODING_UTF8),
// TODO:
                    rresCodeLang = config.codeLang.getOrElse(0),
// TODO:
                    cultureCode = config.cultureCode.getOrElse(0),
                )
            }

            is ImageConfig -> {
// TODO:
                Chunk.RawChunk(
                    size = rawData.size,
                    data = rawData
                )
            }

            else -> {
                error("not implememt")
            }
        }
    }

    companion object {
        fun taskName(name: String) = "pack".appendCapitalized(name, "rres")
    }
}

private fun Sink.writRresFileHeader(resourceCount: Int) {
    // Signature Id
    writeByte('r'.code.toByte())
    writeByte('r'.code.toByte())
    writeByte('e'.code.toByte())
    writeByte('s'.code.toByte())

    // Version
    writeShortLe(100)

    // Resource Count
    writeShortLe(resourceCount.toShort())
// TODO:
    // CD Offset
    writeUIntLe(0u)
    // <reserved>
    writeUIntLe(0u)
}

// write chunk and return written size.
private fun Sink.writeChunk(chunk: Chunk, resourceId: UInt): Int {
    val crc32 = CRC32()
    var size = 0
    // rresResourceChunkInfo
    // Resource chunk type (FourCC)
    writeChunkType(chunk)
    // Resource chunk identifier (generated from filename CRC32 hash)
    writeUIntLe(resourceId)
// TODO:
    // Data compression algorithm
    writeByte(0b0)
// TODO:
    // Data encription algorithm
    writeByte(0b0)
// TODO:
    // Data flags (if required)
    writeShortLe(0)
// TODO: compressed/encrypted file
    // packedSize  Data chunk size (compressed/encrypted + custom data appended)
    writeUIntLe(chunk.chunkDataSize().toUInt())
    // Data base size (uncompressed/unencrypted)
    writeUIntLe(chunk.chunkDataSize().toUInt())
// TODO:
    // Next resource chunk global offset (if resource has multiple chunks)
    writeUIntLe(0u)
// TODO:
    // <reserved>
    writeUIntLe(0u)
    // Data chunk CRC32 (propCount + props[] + data)
    val propCountBytes = Buffer().apply {
        writeUIntLe(chunk.propertyCount().toUInt())
    }.readByteArray()
    val propsBytes = chunk.properties()
    crc32.update(propCountBytes)
    crc32.update(propsBytes)
    crc32.update(chunk.data)
    writeUIntLe(crc32.value.toUInt())

    size += 8 * 4

    // rres resource chunk data
    // Resource chunk properties count
    write(propCountBytes)
    size += 4
    // Resource chunk properties
    write(propsBytes)
    size += 4 * chunk.propertyCount()
    // Resource chunk raw data
    write(chunk.data)
    size += chunk.data.size

    return size
}

private fun Sink.writeChunkType(type: Chunk) {
    when (type) {
        is Chunk.TextChunk -> {
            writeByte('T'.code.toByte())
            writeByte('E'.code.toByte())
            writeByte('X'.code.toByte())
            writeByte('T'.code.toByte())
        }

        is Chunk.ImageChunk -> {
            writeByte('I'.code.toByte())
            writeByte('M'.code.toByte())
            writeByte('G'.code.toByte())
            writeByte('E'.code.toByte())
        }

        is Chunk.RawChunk -> {
            writeByte('R'.code.toByte())
            writeByte('A'.code.toByte())
            writeByte('W'.code.toByte())
            writeByte('D'.code.toByte())
        }

        is Chunk.CentralDirectoryChunk -> {
            writeByte('C'.code.toByte())
            writeByte('D'.code.toByte())
            writeByte('I'.code.toByte())
            writeByte('R'.code.toByte())
        }
    }
}

private fun Sink.writeDir(info: ResourceInfo) {
    // Resource id
    writeUIntLe(info.resourceId)
    // Resource global offset in file
    writeUIntLe(info.offset.toUInt())
    // reserved
    writeUIntLe(info.reserved.toUInt())
    // Resource fileName size (NULL terminator and 4-byte alignment padding considered)

    val nameBytes = info.fileName.toByteArray(Charsets.UTF_8)
    val stringByteLength = nameBytes.size + 1
    val padding = 4 - (stringByteLength % 4)
    val finalFileNameSize = stringByteLength + padding
    writeUIntLe(finalFileNameSize.toUInt())
    write(nameBytes)
    writeByte(0)
    repeat(padding) {
        writeByte(0)
    }
}

private sealed interface Chunk {
    val data: ByteArray

    companion object {
        private const val PROP_SIZE = 4
    }

    class RawChunk(val size: Int, override val data: ByteArray) : Chunk {

        override fun propertyCount() = 1

        override fun properties(): ByteArray {
            return Buffer().apply {
                writeUIntLe(size.toUInt())
            }.readByteArray()
        }
    }

    class TextChunk(
        val size: Int,
        val rresTextEncoding: TextEncoding,
        val rresCodeLang: Int,
        val cultureCode: Int,
        override val data: ByteArray
    ) : Chunk {

        override fun propertyCount() = 4

        override fun properties(): ByteArray {
            return Buffer().apply {
                writeUIntLe(this@TextChunk.size.toUInt())
                writeUIntLe(rresTextEncoding.value)
                writeUIntLe(rresCodeLang.toUInt())
                writeUIntLe(cultureCode.toUInt())
            }.readByteArray()
        }
    }

    class ImageChunk(
        val width: Int,
        val height: Int,
        val rresPixelFormat: Int,
        val mipmaps: Int,
        override val data: ByteArray
    ) : Chunk {

        override fun propertyCount() = 4

        override fun properties(): ByteArray {
            return Buffer().apply {
                writeUIntLe(width.toUInt())
                writeUIntLe(height.toUInt())
                writeUIntLe(rresPixelFormat.toUInt())
                writeUIntLe(mipmaps.toUInt())
            }.readByteArray()
        }
    }

    class CentralDirectoryChunk(
        val infos: List<ResourceInfo>
    ) : Chunk {
        override val data: ByteArray = run {
            Buffer().apply {
                infos.forEach {
                    writeDir(it)
                }
            }.readByteArray()
        }

        override fun propertyCount(): Int = 1

        override fun properties(): ByteArray {
            return Buffer().apply {
                writeUIntLe(infos.size.toUInt())
            }.readByteArray()
        }
    }

    fun propertyCount(): Int
    fun properties(): ByteArray
    fun chunkDataSize(): Int {
        return (propertyCount() + 1) * PROP_SIZE + data.size
    }
}

private fun generateResourceId(fileName: String): UInt {
    val crc32 = CRC32()
    crc32.update(fileName.toByteArray(Charsets.UTF_8))
    return crc32.value.toUInt()
}

private data class ResourceInfo(
    val resourceId: UInt,
    val fileName: String,
    val offset: Int,
    val reserved: Int = 0,
)
