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
            api(project(":raylib-gui"))
            api(project(":raylib-kt-core"))
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
