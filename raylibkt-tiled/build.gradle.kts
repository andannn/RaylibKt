plugins {
    id("kmp.library")
    alias(libs.plugins.serialization)
}

kmpExtension {
    macosArm64()
    androidNativeArm64()

    withSourceSets {
        it.all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        it.commonMain.dependencies {
            api(project(":raylibkt-foundation"))
            api(project(":raylibkt-components"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
