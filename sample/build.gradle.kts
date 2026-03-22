plugins {
    id("io.github.andannn.raylibkt")
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.task.tree)
}

kotlin {
    macosArm64 {
        binaries {
            executable {
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
    }

    android {
        namespace = "me.raylib.sample"
        compileSdk = 36
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
                add("-Wl,--wrap=fopen")
            }
        }
    }
    sourceSets {
        nativeMain.dependencies {
            implementation(project(":raylibkt-gui"))
            implementation(project(":raylibkt-rres"))
            implementation(project(":raylibkt-easings"))
            implementation(project(":raylibkt-tiled"))
            implementation(project(":raylibkt-components"))
            implementation(libs.kotlinx.io)
        }
    }

    compilerOptions {
        // https://kotlinlang.org/docs/whatsnew22.html#preview-of-context-parameters
        freeCompilerArgs.add("-Xcontext-parameters")
        freeCompilerArgs.add("-Xreturn-value-checker=check")
    }
    sourceSets.all {
        languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }

// TODO: this is workaround for can not bundle rres to android assets in kmp module.
    val rresArtifactType = Attribute.of("com.yourgame.artifact.type", String::class.java)

    val rresElements by configurations.creating {
        isCanBeConsumed = true
        isCanBeResolved = false
        attributes.attribute(rresArtifactType, "rres-binary-dir")
    }

    project.extensions.getByType<GameAssetsExtension>().rresAssets.all {
        artifacts {
            add(rresElements.name, packageTaskProvider.flatMap { it.outputDir })
        }
    }
}

gameAssets {
    rresAssets.create("app") {
        baseDir = project.layout.projectDirectory.dir("resources")
        resources {
            register<TextConfig>("tiled/test.tmj")
            register<TextConfig>("tiled/tilesets")
            register<TextConfig>("tiled/template")
            register<ImageConfig>("tiled/img")
            register<ImageConfig>("cat.png")
            register<ImageConfig>("explosion.png")
            register<ImageConfig>("scarfy.png")
        }
    }

    rawAssets.create("res") {
        dir = project.layout.projectDirectory.dir("resources")
    }
}
