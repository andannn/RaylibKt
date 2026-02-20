plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    api(project(":common"))
    implementation(libs.android.kmp.library.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("KmpExtLibraryPlugin") {
            id = "kmp.ext"
            implementationClass = "KmpExtLibraryPlugin"
        }
    }
}
