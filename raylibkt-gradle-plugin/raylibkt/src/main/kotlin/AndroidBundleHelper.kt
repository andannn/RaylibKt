import com.android.build.api.variant.HasDeviceTests
import com.android.build.api.variant.SourceDirectories
import com.android.build.api.variant.Sources
import com.android.build.api.variant.Variant
import com.android.utils.appendCapitalized
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.konan.target.Family
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * add Android native shared libraries(.so) to android libs folder.
 *
 * @param kotlinTargets kotlin targets
 * @param buildType build type of shared librarys
 */
internal fun Project.addKotlinNativeSharedLibrariesToLibsDir(
    kotlinTargets: NamedDomainObjectCollection<KotlinTarget>,
    buildType: NativeBuildType,
) {
    addNativeLibrariesToAndroidVariantSources(
        prefix = "kotlinNativeSo",
        forTest = false,
        configureCombineTaskAction = {
            this.configureFrom(kotlinTargets, buildType) { it.konanTarget.family == Family.ANDROID }
        },
    )
}

fun Project.wireRresTaskToAndroidAssets(
    packages: NamedDomainObjectCollection<RresPackageConfig>,
    forTest: Boolean = false,
) {
    addGeneratedToAndroidVariantSources(
        forTest = forTest,
        provideSourceDirectories = { assets },
        setup = { variant, itemName, sources ->
            checkNotNull(sources) {
                "Cannot find assets for variant: $variant (forTest=$forTest)"
            }
            packages.configureEach {
                sources.addGeneratedSourceDirectory(
                    taskProvider = packageTaskProvider,
                    wiredWith = { it.outputDir },
                )
            }
        }
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
                val objectFileProvider = binary.linkTaskProvider.flatMap { linkTaskProvider ->
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

fun Project.addNativeLibrariesToAndroidVariantSources(
    prefix: String,
    forTest: Boolean,
    configureCombineTaskAction: TaskProvider<CombineObjectFilesTask>.() -> Unit,
) {
    addGeneratedToAndroidVariantSources(
        forTest = forTest,
        provideSourceDirectories = { jniLibs },
        setup = { variant, itemName, sources ->
            checkNotNull(sources) {
                "Cannot find jni libs sources for variant: $variant (forTest=$forTest)"
            }
            val combineTask =
                project.tasks.register(
                    "createJniLibsDirectoryFor"
                        .appendCapitalized(
                            prefix,
                            "for",
                            itemName,
                            name,
                        ),
                    CombineObjectFilesTask::class.java,
                )
            combineTask.apply(configureCombineTaskAction)

            sources.addGeneratedSourceDirectory(
                taskProvider = combineTask,
                wiredWith = { it.outputDirectory },
            )
        }
    )
}

fun Project.addGeneratedToAndroidVariantSources(
    forTest: Boolean,
    provideSourceDirectories: Sources.() -> (SourceDirectories?),
    setup: (
        variant: Variant,
        itemName: String,
        sources: SourceDirectories?,
    ) -> Unit,
) {
    project.androidExtension.onVariants(project.androidExtension.selector().all()) { variant ->
        if (forTest) {
            check(variant is HasDeviceTests) { "Variant $variant does not have a test target" }
            variant.deviceTests.forEach { (_, deviceTest) ->
                setup(variant, deviceTest.name, provideSourceDirectories(deviceTest.sources))
            }
        } else {
            setup(variant, variant.name, provideSourceDirectories(variant.sources))
        }
    }
}
