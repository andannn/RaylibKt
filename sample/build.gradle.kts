plugins {
    alias(libs.plugins.kotlinMultiplatform)
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
    sourceSets.all {
        languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }
    sourceSets {
        macosArm64Main.dependencies {
            implementation(project(":raylib"))
        }
    }
}

