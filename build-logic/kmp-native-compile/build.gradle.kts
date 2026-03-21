plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.tool.common)
    implementation(libs.spotless.gradlePlugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.maven.publish.gradle.plugin)
    implementation(libs.android.kmp.library.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("KmpNativeCompileLibraryPlugin") {
            id = "kmp.library"
            implementationClass = "KmpNativeCompileLibraryPlugin"
        }
    }
}
