plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    api(libs.android.tool.common)
    api(libs.spotless.gradlePlugin)
    implementation(libs.android.kmp.library.gradlePlugin)
}