import java.awt.image.BufferedImage

enum class PixelFormat(
    val value: UInt
) {
    RRES_PIXELFORMAT_UNDEFINED(0u),

    // 8 bit per pixel (no alpha)
    RRES_PIXELFORMAT_UNCOMP_GRAYSCALE(1u),

    // 16 bpp (2 channels)
    RRES_PIXELFORMAT_UNCOMP_GRAY_ALPHA(2u),

    // 16 bpp
    RRES_PIXELFORMAT_UNCOMP_R5G6B5(3u),

    // 24 bpp
    RRES_PIXELFORMAT_UNCOMP_R8G8B8(4u),

    // 16 bpp (1 bit alpha)
    RRES_PIXELFORMAT_UNCOMP_R5G5B5A1(5u),

    // 16 bpp (4 bit alpha)
    RRES_PIXELFORMAT_UNCOMP_R4G4B4A4(6u),

    // 32 bpp
    RRES_PIXELFORMAT_UNCOMP_R8G8B8A8(7u),

    // 32 bpp (1 channel - float)
    RRES_PIXELFORMAT_UNCOMP_R32(8u),

    // 32*3 bpp (3 channels - float)
    RRES_PIXELFORMAT_UNCOMP_R32G32B32(9u),

    // 32*4 bpp (4 channels - float)
    RRES_PIXELFORMAT_UNCOMP_R32G32B32A32(10u),

    // 4 bpp (no alpha)
    RRES_PIXELFORMAT_COMP_DXT1_RGB(11u),

    // 4 bpp (1 bit alpha)
    RRES_PIXELFORMAT_COMP_DXT1_RGBA(12u),

    // 8 bpp
    RRES_PIXELFORMAT_COMP_DXT3_RGBA(13u),

    // 8 bpp
    RRES_PIXELFORMAT_COMP_DXT5_RGBA(14u),

    // 4 bpp
    RRES_PIXELFORMAT_COMP_ETC1_RGB(15u),

    // 4 bpp
    RRES_PIXELFORMAT_COMP_ETC2_RGB(16u),

    // 8 bpp
    RRES_PIXELFORMAT_COMP_ETC2_EAC_RGBA(17u),

    // 4 bpp
    RRES_PIXELFORMAT_COMP_PVRT_RGB(18u),

    // 4 bpp
    RRES_PIXELFORMAT_COMP_PVRT_RGBA(19u),

    // 8 bpp
    RRES_PIXELFORMAT_COMP_ASTC_4x4_RGBA(20u),

    // 2 bpp
    RRES_PIXELFORMAT_COMP_ASTC_8x8_RGBA(21u),
}

fun detectPixelFormat(bufferImageType: Int): PixelFormat {
    return when (bufferImageType) {
        // 24-bit RGB (888)
        BufferedImage.TYPE_INT_RGB,
        BufferedImage.TYPE_3BYTE_BGR -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_R8G8B8 // 4u

        // 32-bit ARGB/RGBA (8888)
        BufferedImage.TYPE_INT_ARGB,
        BufferedImage.TYPE_INT_ARGB_PRE,
        BufferedImage.TYPE_4BYTE_ABGR,
        BufferedImage.TYPE_4BYTE_ABGR_PRE -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_R8G8B8A8 // 7u

        // 16-bit RGB (565)
        BufferedImage.TYPE_USHORT_565_RGB -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_R5G6B5 // 3u

        // 16-bit RGB + 1-bit Alpha (5551)
        BufferedImage.TYPE_USHORT_555_RGB -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_R5G5B5A1 // 5u

        // 8-bit Grayscale
        BufferedImage.TYPE_BYTE_GRAY -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_GRAYSCALE // 1u

        // 特殊处理：16-bit Grayscale (Raylib 默认可能不支持直接对应，通常转为灰度或 R32)
        BufferedImage.TYPE_USHORT_GRAY -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_GRAYSCALE // 1u (可能会丢失精度)

        // BGR 类型 (Raylib 通常期望 RGB，如果不需要转换像素，标记为 R8G8B8 可能会导致红蓝反转)
        BufferedImage.TYPE_INT_BGR -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_R8G8B8 // 4u

        // 索引色/二进制图 (建议在构建时转为带 Alpha 的 RGBA 以保证通用性)
        BufferedImage.TYPE_BYTE_BINARY,
        BufferedImage.TYPE_BYTE_INDEXED -> PixelFormat.RRES_PIXELFORMAT_UNCOMP_R8G8B8A8 // 7u

        BufferedImage.TYPE_CUSTOM -> error("Not support custom image type")
        else -> error("Unsupported image type: ")
    }
}

internal  fun BufferedImage.convertToRawPixels(format: PixelFormat): ByteArray {
    val img = this
    val width = img.width
    val height = img.height

    return when (format) {
        PixelFormat.RRES_PIXELFORMAT_UNCOMP_R8G8B8A8 -> {
            val buffer = ByteArray(width * height * 4)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val argb = img.getRGB(x, y)
                    val offset = (y * width + x) * 4
                    buffer[offset] = ((argb shr 16) and 0xFF).toByte() // R
                    buffer[offset + 1] = ((argb shr 8) and 0xFF).toByte()  // G
                    buffer[offset + 2] = (argb and 0xFF).toByte()         // B
                    buffer[offset + 3] = ((argb shr 24) and 0xFF).toByte() // A
                }
            }
            buffer
        }

        PixelFormat.RRES_PIXELFORMAT_UNCOMP_R8G8B8 -> {
            val buffer = ByteArray(width * height * 3) // RGB = 3 bytes
            var offset = 0
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val argb = img.getRGB(x, y)
                    buffer[offset++] = ((argb shr 16) and 0xFF).toByte() // R
                    buffer[offset++] = ((argb shr 8) and 0xFF).toByte()  // G
                    buffer[offset++] = (argb and 0xFF).toByte()         // B
                }
            }
            buffer
        }

        else -> TODO()
    }
}