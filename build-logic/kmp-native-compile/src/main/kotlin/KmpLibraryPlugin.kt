import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.gradle.api.KotlinMultiplatformAndroidPlugin
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

class KmpNativeCompileLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val kmpExtension =
            extensions.create<KMPExtension>(
                KMPExtension.EXTENSION_NAME,
                project,
            )

        pluginManager.apply("com.diffplug.spotless")
        extensions.configure<SpotlessExtension> {
            configureSpotless(project)
        }

        project.plugins.configureEach {
            when (this) {
                is KotlinMultiplatformAndroidPlugin -> {
                    configureWithKotlinMultiplatformAndroidPlugin(project, kmpExtension.agpKmpExtension)
                }
            }
        }

    }

    private fun configureWithKotlinMultiplatformAndroidPlugin(
        project: Project,
        kotlinMultiplatformAndroidTarget: KotlinMultiplatformAndroidLibraryTarget,
    ) {
        kotlinMultiplatformAndroidTarget.apply {
            compileSdk = 36
        }
    }
}
