plugins {
    id("kmp.library")
}

kmpExtension {
    macosArm64()

    androidNativeArm64()
    withSourceSets {
        it.all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        it.commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        it.commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
