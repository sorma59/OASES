plugins {
    id ("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")

    id("com.google.devtools.ksp") version "2.1.20-2.0.0"
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.unimib.oases"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.unimib.oases"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dagger - Hilt
    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler)

    // Compose dependencies
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.hilt.navigation)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //  Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // Kotlin extensions and Room coroutine support
    ksp(libs.androidx.room.compiler)

    //  Retrofit
    implementation(libs.retrofit)

    //  GSON
    implementation(libs.converter.gson)
    implementation(libs.gson)


    // COIL

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.network)

    // SERIALIZE
    implementation(libs.kotlinx.serialization.json)

// ... other dependencies ...
    implementation(libs.androidx.security.crypto) // For EncryptedSharedPreferences
    implementation(libs.jbcrypt) // For BCrypt hashing
    implementation(libs.gson) // For converting object to Json
//    kapt(libs.hilt.android.compiler.v250)
}
