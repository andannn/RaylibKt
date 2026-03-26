plugins {
    id("kotlin.native.compile")
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
            api(project(":raylibkt-foundation"))
            api(project(":raylib-easings"))
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
