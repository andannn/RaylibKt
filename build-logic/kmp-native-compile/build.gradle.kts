plugins {
    `kotlin-dsl`
}

dependencies {
    api(project(":common"))
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
