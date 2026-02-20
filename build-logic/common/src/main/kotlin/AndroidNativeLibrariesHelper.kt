import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.variant.HasDeviceTests
import com.android.build.api.variant.SourceDirectories
import com.android.build.api.variant.Sources
import com.android.utils.appendCapitalized
import org.gradle.api.tasks.TaskProvider

fun KotlinMultiplatformAndroidLibraryTarget.addNativeLibrariesToAndroidVariantSources(
    prefix: String,
    forTest: Boolean,
    configureCombineTaskAction: TaskProvider<CombineObjectFilesTask>.() -> Unit,
    provideSourceDirectories: Sources.() -> (SourceDirectories.Layered?),
) {
    project.androidExtension.onVariants(project.androidExtension.selector().all()) { variant ->
        fun setup(
            itemName: String,
            sources: SourceDirectories.Layered?,
        ) {
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

        if (forTest) {
            check(variant is HasDeviceTests) { "Variant $variant does not have a test target" }
            variant.deviceTests.forEach { (_, deviceTest) ->
                setup(deviceTest.name, provideSourceDirectories(deviceTest.sources))
            }
        } else {
            setup(variant.name, provideSourceDirectories(variant.sources))
        }
    }
}
