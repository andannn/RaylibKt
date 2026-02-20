plugins {
    alias(libs.plugins.android.application)
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
}

dependencies {
    implementation(project(":sample"))
}
