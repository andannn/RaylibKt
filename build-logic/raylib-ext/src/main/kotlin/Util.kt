import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

val Project.androidExtension: AndroidComponentsExtension<*, *, *>
    get() =
        extensions.findByType<KotlinMultiplatformAndroidComponentsExtension>()
            ?: throw IllegalArgumentException("Failed to find any registered Android extension")
