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
    abstract val className: Property<String>
    @get:InputFile
    abstract val mappingFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        description = "Generate ResourceId constants source file"
        group = "Build"
    }

    @TaskAction
    fun generate() {
        val mappingTextSource = mappingFile.get().asFile.inputStream().asSource().buffered()
        val pkgName = targetPackage.get()
        val clsName = className.get()
        val outDir = outputDir.get().asFile

        val objectBuilder = TypeSpec.objectBuilder(clsName)
            .addKdoc("Auto-generated Raylib Resource IDs.\nDO NOT MODIFY MANUALLY.")

        while (!mappingTextSource.exhausted()) {
            val line = mappingTextSource.readLine() ?: break
            val parts = line.split(",")
            val relativePath = parts[0]
            val resourceId = parts[1]
            val varName = relativePath
                .replace("/", "_")
                .replace(".", "_")
                .replace("-", "_")
                .lowercase()

            val propertySpec = PropertySpec.builder(varName, UInt::class)
                .addModifiers(KModifier.CONST)
                .initializer("%Lu", resourceId)
                .build()
            objectBuilder.addProperty(propertySpec)
        }

        val fileSpec = FileSpec.builder(pkgName, clsName)
            .addType(objectBuilder.build())
            .build()

        fileSpec.writeTo(outDir)

        println("[RresCodeGen] Successfully generated $pkgName.$clsName using KotlinPoet at: ${outDir.absolutePath}")
    }
}