import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

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
        createNativeCompilation("rayLibCompile") {
            configureEachTarget {
                val externalRaylibPath = project.layout.projectDirectory.dir("external/raylib_c")
                val externalRResPath = project.layout.projectDirectory.dir("external/rres")
                val sourceList = buildList {
                    add("${externalRaylibPath}/src/rcore.c")
                    add("${externalRaylibPath}/src/rshapes.c")
                    add("${externalRaylibPath}/src/utils.c")
                    add("${externalRaylibPath}/src/rtextures.c")
                    add("${externalRaylibPath}/src/rmodels.c")
                    add("${externalRaylibPath}/src/raudio.c")
                    add("${externalRaylibPath}/src/rtext.c")
                    add("c/include_rres.c")
                    if (konanTarget.isDesktop()) {
                        add("rglfw.c")
                    }
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
                    sources.from(project.layout.projectDirectory.dir("external/native_app_glue/android_native_app_glue.c"))
                    sources.from(project.layout.projectDirectory.dir("src/androidNativeMain/c/android_main.c"))
                    includes.from(project.layout.projectDirectory.dir("external/native_app_glue/"))
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
                includes.from(externalRResPath.dir("src"))
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

mavenPublishing {
    configureMaven(project)
}
