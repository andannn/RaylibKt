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
            api(project(":raylib-kt-core"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
