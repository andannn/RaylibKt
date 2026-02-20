plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    api(libs.android.tool.common)
    implementation(libs.android.kmp.library.gradlePlugin)
}