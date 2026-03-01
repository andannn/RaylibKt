rootProject.name = "RaylibKt"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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

include(":raylib-core")
include(":raylib-kt-core")
include(":raylib-easings")
include(":raylib-kt-easings")
include(":raylib-gui")
include(":raylib-kt-gui")
include(":raylib-kt-framework")
include(":raylib-kt-components")
include(":sample")
include(":sample-android-app")
