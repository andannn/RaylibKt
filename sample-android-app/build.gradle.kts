plugins {
    alias(libs.plugins.android.application)
}

val rresArtifactType = Attribute.of("com.yourgame.artifact.type", String::class.java)

val rresFiles by project.configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes.attribute(rresArtifactType, "rres-binary-dir") // 凭标签取货
}

dependencies {
    rresFiles(project(":sample"))
    implementation(project(":sample"))
}

android {
    namespace = "me.sample.native_activity"

    defaultConfig {
        applicationId = "me.sample.native_activity"
        // This is the minimum required for using Choreographer directly from the NDK. If you need
        // to use a lower minSdkVersion, you must use the Java Choreographer API via JNI.
        minSdk = 24
        compileSdk = 36
    }
    sourceSets {
        getByName("main") {
            assets.srcDir(rresFiles)
        }
    }
}

tasks.configureEach {
    if (name.startsWith("merge") && name.endsWith("Assets")) {
        dependsOn(rresFiles)
    }
}
