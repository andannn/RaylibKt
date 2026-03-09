plugins {
    id("kmp.library")
    id("com.dorongold.task-tree")
}

kmpExtension {
    macosArm64()
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
            api(project(":raylib-gui"))
            api(project(":raylib-kt-core"))
        }
    }
}