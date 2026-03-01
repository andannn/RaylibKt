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
include(":raylib-core-ktx")
include(":raylib-easings")
include(":raylib-easings-ktx")
include(":raylib-gui")
include(":raylib-gui-ktx")
include(":raylib-kt-framework")
include(":raylib-kt-components")
include(":sample")
include(":sample-android-app")
