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
            add(KonanTarget.ANDROID_ARM64)
            add(KonanTarget.ANDROID_X86)
            add(KonanTarget.ANDROID_X64)
        }
    val rayLibCompile =
        createNativeCompilation("rayLibCompile") {
            configureEachTarget {
                val externalRaylibPath = project.layout.projectDirectory.dir("external")
                val sourceList = buildList {
                    add("rcore.c")
                    add("rshapes.c")
                    add("utils.c")
                    add("rtextures.c")
                    add("rmodels.c")
                    add("raudio.c")
                    add("rtext.c")
                    if (konanTarget.isDesktop()) {
                        add("rglfw.c")
                    }
                }.map {
                    externalRaylibPath.dir("src/$it")
                }

                if (konanTarget.family == Family.OSX) {
                    freeArgs.add("-x")
                    freeArgs.add("objective-c")
                }
                if (konanTarget.family == Family.ANDROID) {
                    freeArgs.add("-D")
                    freeArgs.add("PLATFORM_ANDROID")
                    freeArgs.add("-D")
                    freeArgs.add("GRAPHICS_API_OPENGL_ES2")
                    sources.from(
                        externalRaylibPath
                            .dir("projects/VS2019-Android/raylib_android/raylib_android.NativeActivity/android_native_app_glue.c")
                    )
                    includes.from(
                        externalRaylibPath
                            .dir("projects/VS2019-Android/raylib_android/raylib_android.NativeActivity")
                    )
                }
                if (konanTarget.family == Family.LINUX) {
                }
                if (konanTarget.isDesktop()) {
                    freeArgs.add("-D")
                    freeArgs.add("PLATFORM_DESKTOP")
                    includes.from(externalRaylibPath.dir("src/external/glfw/include"))
                }

                sources.from(sourceList)
                includes.from(externalRaylibPath.dir("src"))
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

private fun KonanTarget.isDesktop(): Boolean {
    if (family == Family.OSX || family == Family.LINUX) return true
    return false
}