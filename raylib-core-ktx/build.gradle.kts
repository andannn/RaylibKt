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
// no X11 dependency on macos
//    linuxX64()
//    linuxArm64()
    androidNativeX64()
    androidNativeX86()
    androidNativeArm32()
    androidNativeArm64()
    withSourceSets {
        it.all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        it.commonMain.dependencies {
            api(project(":raylib-core"))
            implementation(libs.kotlinx.coroutines.core)
        }

        it.commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
