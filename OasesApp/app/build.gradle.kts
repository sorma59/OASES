import java.util.Properties

plugins {
    id ("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")

    id("com.google.devtools.ksp") version "2.2.20-2.0.3"
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
//    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.unimib.oases"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.unimib.oases"
        minSdk = 29
        targetSdk = 36
        versionCode = 6
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keyFile = "gradle.local.properties"

    signingConfigs {
        val localPropertiesFile = rootProject.file(keyFile)
        if (localPropertiesFile.exists()) {
            val localProperties = Properties().apply {
                localPropertiesFile.reader().use { load(it) }
            }
            create("release") {
                storeFile = file(localProperties["KEYSTORE_FILE"] ?: "")
                storePassword = localProperties["KEYSTORE_PASSWORD"] as String?
                keyAlias = localProperties["KEY_ALIAS"] as String?
                keyPassword = localProperties["KEY_PASSWORD"] as String?
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
        }
        val signing = signingConfigs.findByName("release")
        if (signing != null){
            release {
                signingConfig = signing
                isMinifyEnabled = false
                isShrinkResources = false
            }
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
//    implementation(libs.firebase.auth)
    implementation(libs.androidx.animation.core.android)
//    implementation(libs.firebase.storage.ktx)
//    implementation(libs.firebase.database.ktx)
//    implementation(libs.firebase.firestore)
    implementation(libs.androidx.material3.window.size.class1.android)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.androidx.compose.runtime) // For serializing Routes in MainScaffold
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

    implementation("com.seanproctor:datatable-material3:0.11.6")

}
