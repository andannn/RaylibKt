import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    id("kmp.ext")
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.task.tree)
}

kotlin {
    macosArm64 {
        binaries.executable {
            entryPoint("me.raylib.sample.main")
            linkerOpts(
                "-framework", "CoreVideo",
                "-framework", "CoreGraphics",
                "-framework", "AppKit",
                "-framework", "IOKit",
                "-framework", "OpenGL",
                "-framework", "Cocoa"
            )
        }
    }

    android {
        namespace = "me.raylib.sample"
        compileSdk = 36

        addKotlinNativeSharedLibrariesToLibsDir(
            kotlinTargets = this@kotlin.targets,
            buildType = NativeBuildType.DEBUG,
        )
    }

    listOf(
//        androidNativeArm32(),
        androidNativeArm64(),
//        androidNativeX86(),
//        androidNativeX64(),
    ).forEach {
        it.binaries.sharedLib {
            baseName = "raygame"
            linkerOpts.apply {
                add("-lm")
                add("-lc")
                add("-llog")
                add("-landroid")
                add("-lEGL")
                add("-lGLESv2")
                add("-lOpenSLES")
                add("-ldl")
                add("-Wl,--undefined=ANativeActivity_onCreate")
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }
    sourceSets {
        nativeMain.dependencies {
            implementation(project(":raylib"))
        }
    }
}

