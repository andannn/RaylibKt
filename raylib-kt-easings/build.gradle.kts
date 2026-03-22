plugins {
    id("kmp.library")
    id("com.dorongold.task-tree")
}

kmpExtension {
    macosArm64()
    androidNativeArm64()

    withSourceSets {
        it.all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        it.commonMain.dependencies {
            api(project(":raylib-kt-foundation"))
            api(project(":raylib-easings"))
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
