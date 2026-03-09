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
            api(project(":raylib-base"))
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
