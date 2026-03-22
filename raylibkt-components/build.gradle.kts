plugins {
    id("kmp.library")
}

kmpExtension {
    macosArm64 {
        binaries {
            getTest("debug").apply {
                linkerOpts(
                    "-framework", "Foundation",
                    "-framework", "AppKit",
                    "-framework", "IOKit",
                    "-framework", "CoreVideo",
                    "-framework", "CoreGraphics",
                    "-framework", "QuartzCore",
                    "-framework", "OpenGL"
                )
            }
        }
    }
    androidNativeArm64()

    withSourceSets {
        it.all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
        it.commonMain.dependencies {
            implementation(project(":raylibkt-runtime"))
            implementation(project(":raylibkt-rres"))
            implementation(project(":raylibkt-easings"))
            implementation(libs.kotlinx.io)
        }
    }
}

mavenPublishing {
    configureMaven(project)
}
