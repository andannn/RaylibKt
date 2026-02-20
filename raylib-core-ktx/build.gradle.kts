plugins {
    id("kmp.library")
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
        it.commonMain.dependencies {
            api(project(":raylib-core"))
        }
    }
}
