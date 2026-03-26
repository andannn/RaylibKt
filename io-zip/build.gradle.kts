plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.task.tree)
}

kotlin {
    macosArm64()
    androidNativeArm64()

    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.io)
        }
    }
}
