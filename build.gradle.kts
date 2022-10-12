plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.diffplug.spotless")
}

version = "0.0.14"
group = "com.keygenqt.response"

spotless {
    kotlin {
        target("**/*.kt")
        licenseHeaderFile("$buildDir/../LICENSE")
    }
}

android {

    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33
        setProperty("archivesBaseName", "compose-requests-$version")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = findProperty("composeCompilerVersion").toString()
    }

    buildFeatures {
        compose = true
    }
}

// https://developer.android.com/jetpack/androidx/releases/compose
val composeVersion = "1.2.1"
// https://github.com/Kotlin/kotlinx.coroutines/releases
val coroutinesVersion = "1.6.4"
// https://developer.android.com/jetpack/androidx/releases/lifecycle
val lifecycleVersion = "2.5.1"

dependencies {
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
}