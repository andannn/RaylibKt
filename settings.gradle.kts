rootProject.name = "RaylibKt"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    includeBuild("raylibkt-gradle-plugin")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":raylib-base")
include(":raylib-rres")
include(":raylib-easings")
include(":raylib-gui")
include(":raylibkt-easings")
include(":raylibkt-assets")
include(":raylibkt-gui")
include(":raylibkt-tiled")
include(":raylibkt-runtime")
include(":raylibkt-foundation")
include(":raylibkt-components")
include(":sample")
include(":sample-android-app")
