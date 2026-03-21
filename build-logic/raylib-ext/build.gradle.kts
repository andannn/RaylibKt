plugins {
    `kotlin-dsl`
    alias(libs.plugins.maven.publish)
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
            id = "raylibkt.ext"
            implementationClass = "RaylibExtPlugin"
        }
    }
}

mavenPublishing {
    signAllPublications()
    publishToMavenCentral()

    pom {
        name.set("RaylibKt")
        description.set("Simple Kotlin Native wrapper for raylib")
        url.set("https://github.com/andannn/RaylibKt")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("andannn")
                name.set("Andannn")
            }
        }

        scm {
            url.set("https://github.com/andannn/RaylibKt.git")
            connection.set("scm:git:git://github.com/andannn/RaylibKt.git")
            developerConnection.set("scm:git:ssh://git@github.com/andannn/RaylibKt.git")
        }
    }
}
