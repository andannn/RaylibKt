import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.readLine
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
abstract class GenerateResourceIdKtTask : DefaultTask() {
    @get:Input
    abstract val targetPackage: Property<String>
    @get:Input
    abstract val rresFileName: Property<String>
    @get:Input
    abstract val className: Property<String>
    @get:InputFile
    abstract val mappingFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val mappingFileRef = mappingFile.get().asFile
        if (!mappingFileRef.exists()) return

        val pkgName = targetPackage.get()
        val clsName = className.get()
        val outDir = outputDir.get().asFile
        val rresFileName = rresFileName.get()

        val rawObj = TypeSpec.objectBuilder("raw")
        val textObj = TypeSpec.objectBuilder("text")
        val imageObj = TypeSpec.objectBuilder("image")

        mappingFileRef.forEachLine { line ->
            if (line.isBlank()) return@forEachLine

            val parts = line.split(",")
            if (parts.size < 3) return@forEachLine

            val relativePath = parts[0]
            val resourceId = parts[1]
            val type = parts[2].lowercase()

            val varName = sanitizeVarName(relativePath)

            val property = PropertySpec.builder(varName, UInt::class)
                .addModifiers(KModifier.CONST)
                .initializer("%Lu", resourceId)
                .build()

            when (type) {
                "raw" -> rawObj.addProperty(property)
                "text" -> textObj.addProperty(property)
                "image" -> imageObj.addProperty(property)
            }
        }

        val fileNameProperty = PropertySpec.builder("rresFile", String::class)
            .addModifiers(KModifier.CONST)
            .initializer("%S", rresFileName)
            .build()

        val resObject = TypeSpec.objectBuilder(clsName)
            .addKdoc("Auto-generated Raylib Resource IDs.\nDO NOT MODIFY MANUALLY.")
            .addProperty(fileNameProperty)
            .addType(rawObj.build())
            .addType(textObj.build())
            .addType(imageObj.build())
            .build()

        FileSpec.builder(pkgName, clsName)
            .addType(resObject)
            .build()
            .writeTo(outDir)

        println("[RresCodeGen] Successfully generated $pkgName.$clsName with nested types.")
    }

    private fun sanitizeVarName(path: String): String {
        return path
            .replace("[^a-zA-Z0-9]".toRegex(), "_")
            .replace("_+".toRegex(), "_")
            .trim('_')
            .lowercase()
    }
}