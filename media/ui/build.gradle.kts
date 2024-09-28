/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.library")
    id(libs.plugins.dokka.get().pluginId)
    alias(libs.plugins.metalavaGradle)
    kotlin("android")
    alias(libs.plugins.roborazzi)
    kotlin("plugin.serialization")
    alias(libs.plugins.compose.compiler)
}

android {
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = false
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
        // Allow for widescale experimental APIs in Alpha libraries we build upon
        freeCompilerArgs = freeCompilerArgs +
            """
            com.google.android.horologist.annotations.ExperimentalHorologistApi
            kotlin.RequiresOptIn
            kotlinx.coroutines.ExperimentalCoroutinesApi
            androidx.wear.compose.material.ExperimentalWearMaterialApi
            """.trim().split("\\s+".toRegex()).map {
                "-opt-in=$it"
            }
    }

    packaging {
        resources {
            excludes +=
                listOf(
                    "/META-INF/AL2.0",
                    "/META-INF/LGPL2.1",
                )
        }
    }

    sourceSets.getByName("main") {
        assets.srcDir("src/main/assets")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }
    lint {
        checkReleaseBuilds = false
        textReport = true
        disable += listOf("MissingTranslation", "ExtraTranslation")

        // https://buganizer.corp.google.com/issues/328279054
        disable.add("UnsafeOptInUsageError")
    }
    namespace = "com.google.android.horologist.media.ui"
}

project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    // Workaround for https://youtrack.jetbrains.com/issue/KT-37652
    if (!this.name.endsWith("TestKotlin") && !this.name.startsWith("compileDebug")) {
        compilerOptions {
            freeCompilerArgs.add("-Xexplicit-api=strict")
        }
    }
}

metalava {
    sourcePaths.setFrom("src/main")
    filename.set("api/current.api")
    reportLintsAsErrors.set(true)
}

dependencies {
    api(projects.annotations)
    api(projects.media.core)
    api(projects.tiles)

    api(projects.composables)
    api(libs.wearcompose.material)
    api(libs.wearcompose.foundation)

    implementation(projects.media.audio)
    implementation(projects.media.audioUi)
    implementation(projects.composeLayout)
    implementation(projects.composeMaterial)
    implementation(projects.images.coil)
    implementation(projects.tiles)
    api(libs.wearcompose.navigation)
    implementation(libs.kotlinx.serialization.core)

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.wear)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodelktx)
    implementation(libs.compose.material.iconscore)
    implementation(libs.compose.material.iconsext)
    implementation(libs.compose.material.ripple)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.lottie.compose)

    implementation(libs.androidx.complications.datasource.ktx)
    implementation(libs.androidx.wear.tiles)
    implementation(libs.androidx.wear.protolayout.material)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.toolingpreview)
    implementation(libs.androidx.constraintlayout.compose)

    debugImplementation(projects.logo)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(projects.media.audioUi)
    debugImplementation(projects.composeTools)
    releaseCompileOnly(projects.composeTools)

    debugImplementation(libs.androidx.complications.rendering)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext.ktx)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
    testImplementation(projects.media.audio)
    testImplementation(projects.media.audioUi)
    testImplementation(projects.roboscreenshots)
    testImplementation(projects.logo)
    testImplementation(libs.compose.ui.test.junit4)
    testImplementation(libs.androidx.test.espressocore)
    testImplementation(libs.androidx.test.ext)
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
        configureEach {
            moduleName.set("media-ui")
        }
    }
}

apply(plugin = "com.vanniktech.maven.publish")
