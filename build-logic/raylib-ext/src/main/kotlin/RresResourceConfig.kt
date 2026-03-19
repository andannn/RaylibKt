import org.gradle.api.Named
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import javax.inject.Inject

interface RresResourceConfig : Named

abstract class RawConfig @Inject constructor(
    @get:Internal val relativePath: String
) : RresResourceConfig {
    override fun getName(): String = relativePath
}

abstract class TextConfig @Inject constructor(
    @get:Internal val relativePath: String
) : RresResourceConfig {
    @Internal
    override fun getName(): String = relativePath

    @get:Optional
    @get:Input
    abstract val textEncoding: Property<TextEncoding>

    @get:Optional
    @get:Input
    abstract val codeLang: Property<Int>

    @get:Optional
    @get:Input
    abstract val cultureCode: Property<Int>
}

abstract class ImageConfig @Inject constructor(
    @get:Internal val relativePath: String
) : RresResourceConfig {
    @Internal
    override fun getName(): String = relativePath

    @get:Optional
    @get:Input
    abstract val width: Property<Int>

    @get:Optional
    @get:Input
    abstract val height: Property<Int>

    @get:Optional
    @get:Input
    abstract val pixelFormat: Property<Int>

    @get:Optional
    @get:Input
    abstract val mipmaps: Property<Int>
}

enum class TextEncoding(val value: UInt) {
    RRES_TEXT_ENCODING_UNDEFINED(0u),
    RRES_TEXT_ENCODING_UTF8(1u),
    RRES_TEXT_ENCODING_UTF8_BOM(2u),
    RRES_TEXT_ENCODING_UTF16_LE(10u),
    RRES_TEXT_ENCODING_UTF16_BE(11u),
}
