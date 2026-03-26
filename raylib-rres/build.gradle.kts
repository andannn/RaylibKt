import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

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
        createNativeCompilation("rresCompile") {
            configureEachTarget {
                sources.from(project.layout.projectDirectory.dir("src/nativeInterop/inclue_rres.c"))
                includes.from(project.layout.projectDirectory.dir("external/rres/src"))
                includes.from(rootProject.layout.projectDirectory.dir("raylib-base/external/raylib_c/src"))
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

mavenPublishing {
    configureMaven(project)
}
