import com.android.build.api.variant.HasDeviceTests
import com.android.build.api.variant.SourceDirectories
import com.android.build.api.variant.Sources
import com.android.build.api.variant.Variant
import com.android.utils.appendCapitalized
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

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
