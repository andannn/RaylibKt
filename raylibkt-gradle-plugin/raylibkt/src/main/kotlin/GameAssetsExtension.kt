import com.android.utils.appendCapitalized
import org.gradle.api.Action
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskProvider
import javax.inject.Inject

abstract class GameAssetsExtension @Inject constructor(private val project: Project) {
    abstract val assetsDictionaryName: Property<String>

    val rresAssets =
        project.objects.domainObjectContainer(
            RresPackageConfig::class.java,
            Factory(project = project),
        )

    val rawAssets: NamedDomainObjectContainer<RawAssetFolder> = project.objects.domainObjectContainer(RawAssetFolder::class.java)

    init {
        assetsDictionaryName.convention("assets")
    }

    class Factory(val project: Project)  : NamedDomainObjectFactory<RresPackageConfig> {
        override fun create(name: String): RresPackageConfig {
            val config = project.objects.newInstance(RresPackageConfig::class.java, name, project)
            val packTaskName = RresPackTask.taskName(name)
            val outputMappingFile = project.layout.buildDirectory.file("intermediates/rres/$name/mapping.txt")
            val packTask = project.tasks.register(packTaskName, RresPackTask::class.java) {
                baseDirectory.set(config.baseDir)
                fileName.set(name)
                outputDir.set(project.layout.buildDirectory.dir("assets"))
                resourceConfigs.set(project.provider {
                    config.resources.associateBy { it.name }
                })
                mappingFile.set(outputMappingFile)
            }

            val generateSourceTaskName = "generate".appendCapitalized(name, "ResourceIds")
            val genTask = project.tasks.register(generateSourceTaskName, GenerateResourceIdKtTask::class.java) {
                targetPackage.set("rres.resources.$name")
                rresFileName.set("$name.rres")
                className.set("${name.replaceFirstChar { it.uppercase() }}Res")
                mappingFile.set(packTask.flatMap { it.mappingFile })
                outputDir.set(project.layout.buildDirectory.dir("generated/source/rres/$name"))
            }

            project.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                val kotlinExtension = project.extensions.getByName("kotlin") as org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
                kotlinExtension.sourceSets.getByName("commonMain").kotlin.srcDir(genTask.map { it.outputDir })
            }
            return config
        }
    }
}

abstract class RresPackageConfig @Inject internal constructor (
    val packageName: String,
    private val project: Project
) : Named {
    @Internal
    override fun getName() = packageName

    abstract val baseDir: DirectoryProperty

    val packageTaskProvider: TaskProvider<out RresPackTask>
        get() = project.tasks.withType(RresPackTask::class.java).named(RresPackTask.taskName(packageName))

    @get:Internal
    val resources: ExtensiblePolymorphicDomainObjectContainer<RresResourceConfig> =
        project.objects.polymorphicDomainObjectContainer(RresResourceConfig::class.java).apply {
            registerFactory(RawConfig::class.java) { name -> project.objects.newInstance(RawConfig::class.java, name) }
            registerFactory(TextConfig::class.java) { name -> project.objects.newInstance(TextConfig::class.java, name) }
            registerFactory(ImageConfig::class.java) { name -> project.objects.newInstance(ImageConfig::class.java, name) }
        }

    fun resources(action: Action<ExtensiblePolymorphicDomainObjectContainer<RresResourceConfig>>) {
        action.execute(resources)
    }
}

interface RawAssetFolder {
    val name: String
    val dir: DirectoryProperty
}