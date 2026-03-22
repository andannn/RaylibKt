import com.android.build.gradle.api.KotlinMultiplatformAndroidPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.konan.target.Family

class RaylibExtPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val gameAssetsExtension = project.extensions.create("gameAssets", GameAssetsExtension::class.java)

        project.plugins.withType<KotlinMultiplatformAndroidPlugin>() {
            val targets = project.extensions.getByType<KotlinMultiplatformExtension>().targets
            project.addKotlinNativeSharedLibrariesToLibsDir(
                targets,
                NativeBuildType.DEBUG
            )
// TODO: kmp android library plugin do not support assets SourceSet
//            project.wireRresTaskToAndroidAssets(
//                rresExtension.packages
//            )
        }

        project.plugins.withType<KotlinMultiplatformPluginWrapper>() {
            val targets = project.extensions.getByType<KotlinMultiplatformExtension>().targets
            targets.configureEach {
                val target = this
                if (target !is KotlinNativeTarget) return@configureEach
                if (target.konanTarget.family == Family.ANDROID) return@configureEach

                target.binaries.withType<Executable>().configureEach {
                    val executable = this

                    val copyTaskName = "copyAssets_${target.name}_${executable.name}"

                    val copyAssetsTask = project.tasks.register<Copy>(copyTaskName) {
                        group = "build"
                        description = "Copies assets to the output directory of ${executable.name} for ${target.name}"

                        gameAssetsExtension.rresAssets.configureEach {
                            val rresPackage = this

                            from(rresPackage.packageTaskProvider.flatMap { it.outputDir }) {
                                 into(gameAssetsExtension.assetsDictionaryName)
                            }
                        }

                        gameAssetsExtension.rawAssets.configureEach {
                            val folderConfig = this

                            from(folderConfig.dir.map { it.asFile.parentFile }) {
                                include("${folderConfig.dir.get().asFile.name}/**")
                                into(gameAssetsExtension.assetsDictionaryName)
                            }
                        }

                        into(executable.outputDirectory)
                    }

                    executable.linkTaskProvider.configure {
                        dependsOn(copyAssetsTask)
                    }

                    executable.runTaskProvider?.configure {
                        workingDir = executable.outputDirectory.resolve(gameAssetsExtension.assetsDictionaryName.get())
                    }
                }
            }
        }
    }
}
