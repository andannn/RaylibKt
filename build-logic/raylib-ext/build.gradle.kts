plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    api(project(":common"))
    implementation(libs.android.kmp.library.gradlePlugin)
    implementation(libs.kotlinx.io)
    implementation(libs.kotlinpoet)
}

gradlePlugin {
    plugins {
        register("RaylibExtPlugin") {
            id = "io.github.andannn.raylib.ext"
            implementationClass = "RaylibExtPlugin"
        }
    }
}
