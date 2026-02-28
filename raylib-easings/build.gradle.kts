import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

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
    }
    val requiredNativeTargets =
        buildList {
            add(KonanTarget.MACOS_ARM64)
            add(KonanTarget.LINUX_ARM64)
            add(KonanTarget.LINUX_X64)
            add(KonanTarget.ANDROID_ARM32)
            add(KonanTarget.ANDROID_ARM64)
            add(KonanTarget.ANDROID_X86)
            add(KonanTarget.ANDROID_X64)
        }
    val rayLibCompile =
        createNativeCompilation("reasingCompile") {
            configureEachTarget {
                sources.from(project.layout.projectDirectory.dir("src/c/empty.c"))
                includes.from(project.layout.projectDirectory.dir("external/reasings/src"))
            }
            configureTargets(requiredNativeTargets)
        }

    targets.configureEach {
        val target = this
        if (target is KotlinNativeTarget) {
            if (target.konanTarget.family == Family.LINUX) {
                // no X11 dependency on macos
                return@configureEach
            }
            createCinterop(
                nativeTarget = target,
                rayLibCompile,
            )
        }
    }
}