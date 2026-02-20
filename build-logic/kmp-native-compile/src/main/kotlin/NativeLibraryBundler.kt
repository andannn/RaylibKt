/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.variant.HasDeviceTests
import com.android.build.api.variant.SourceDirectories
import com.android.build.api.variant.Sources
import com.android.utils.appendCapitalized
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

/**
 * Helper class to bundle outputs of [MultiTargetNativeCompilation] with a JVM or Android project.
 */
class NativeLibraryBundler(
    private val project: Project,
) {
    /**
     * Adds the shared library outputs from [nativeCompilation] to the resources of the [jvmTarget].
     *
     * @see CombineObjectFilesTask for details.
     */
    fun addNativeLibrariesToResources(
        jvmTarget: KotlinJvmTarget,
        nativeCompilation: MultiTargetNativeCompilation,
        compilationName: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    ) {
        val combineTask =
            project.tasks.register(
                "createCombinedResourceArchiveFor"
                    .appendCapitalized(
                        jvmTarget.name,
                        nativeCompilation.archiveName,
                        compilationName,
                    ),
                CombineObjectFilesTask::class.java,
            ) {
                outputDirectory.set(
                    project.layout.buildDirectory.dir(
                        "combinedNativeLibraries/${jvmTarget.name}/" +
                                "${nativeCompilation.archiveName}/$compilationName",
                    ),
                )
            }
        val jniFamilies = listOf(Family.OSX, Family.MINGW, Family.LINUX)
        combineTask.configureFrom(nativeCompilation) { it.family in jniFamilies }
        jvmTarget.compilations[compilationName]
            .defaultSourceSet
            .resources
            .srcDir(combineTask.map { it.outputDirectory })
    }

    /**
     * Adds the shared library outputs from [nativeCompilation] to a given variant src set of the
     * [androidTarget], expressed with the [provideSourceDirectories].
     *
     * @see CombineObjectFilesTask for details.
     */
    fun addNativeLibrariesToAndroidVariantSources(
        androidTarget: KotlinMultiplatformAndroidLibraryTarget,
        nativeCompilation: MultiTargetNativeCompilation,
        forTest: Boolean,
        provideSourceDirectories: Sources.() -> (SourceDirectories.Layered?),
    ) {
        androidTarget.addNativeLibrariesToAndroidVariantSources(
            prefix = nativeCompilation.archiveName,
            forTest = forTest,
            configureCombineTaskAction = {
                this.configureFrom(nativeCompilation) { it.family == Family.ANDROID }
            },
            provideSourceDirectories = provideSourceDirectories,
        )
    }
}

/**
 * Configures the [CombineObjectFilesTask] with the outputs of the [multiTargetNativeCompilation]
 * based on the given target [filter].
 */
fun TaskProvider<CombineObjectFilesTask>.configureFrom(
    multiTargetNativeCompilation: MultiTargetNativeCompilation,
    filter: (KonanTarget) -> Boolean,
) {
    configure {
        objectFiles.addAll(
            multiTargetNativeCompilation.targetsProvider(filter).map { nativeTargetCompilations ->
                nativeTargetCompilations.map { nativeTargetCompilation ->
                    nativeTargetCompilation.linkerTask.map { linkerTask ->
                        ObjectFile(
                            konanTarget = linkerTask.clangParameters.konanTarget,
                            file = linkerTask.clangParameters.outputFile,
                        )
                    }
                }
            },
        )
    }
}
