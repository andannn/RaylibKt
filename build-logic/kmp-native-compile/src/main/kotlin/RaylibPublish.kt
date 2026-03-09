import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Project

fun MavenPublishBaseExtension.configureMaven(project: Project) {
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