import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.konan.target.Family

/**
 * add Android native shared libraries(.so) to android libs folder.
 *
 * @param kotlinTargets kotlin targets
 * @param buildType build type of shared librarys
 */
fun KotlinMultiplatformAndroidLibraryTarget.addKotlinNativeSharedLibrariesToLibsDir(
    kotlinTargets: NamedDomainObjectCollection<KotlinTarget>,
    buildType: NativeBuildType,
) {
    addNativeLibrariesToAndroidVariantSources(
        prefix = "kotlinNativeSo",
        forTest = false,
        configureCombineTaskAction = {
            this.configureFrom(kotlinTargets, buildType) { it.konanTarget.family == Family.ANDROID }
        },
        provideSourceDirectories = { jniLibs },
    )
}

private fun TaskProvider<CombineObjectFilesTask>.configureFrom(
    kotlinTargets: NamedDomainObjectCollection<KotlinTarget>,
    buildType: NativeBuildType,
    filter: (KotlinNativeTarget) -> Boolean,
) {
    configure {
        kotlinTargets.configureEach {
            val target = this
            if (target is KotlinNativeTarget && filter(target)) {
                val binary = target.binaries.getSharedLib(buildType)
                dependsOn(binary.linkTaskProvider)
                val objectFileProvider = binary.linkTaskProvider.flatMap { linkTaskProvider->
                    linkTaskProvider.outputFile.map { file ->
                        ObjectFile(
                            konanTarget = project.provider { SerializableKonanTarget(target.konanTarget) },
                            file = project.objects.fileProperty().apply { set(file) },
                        )
                    }
                }
                objectFiles.addAll(objectFileProvider)
            }
        }
    }
}
