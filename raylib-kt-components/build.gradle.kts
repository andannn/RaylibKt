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
            implementation(project(":raylib-kt-core"))
            implementation(project(":raylib-kt-easings"))
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
