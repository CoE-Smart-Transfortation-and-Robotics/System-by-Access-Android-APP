plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.telkom.ceostar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.telkom.ceostar"
        minSdk = 24
        targetSdk = 35
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
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("com.squareup:javapoet:1.13.0")

    // Networking - pastikan semua ada
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)

    // Coroutines - wajib untuk Flow dan StateFlow
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle - untuk viewModelScope dan lifecycleScope
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // DataStore - untuk PreferencesManager
    implementation(libs.androidx.datastore.preferences)

    // UI Components
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.recyclerview)
    implementation(libs.glide)
    implementation(libs.lottie)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Hindari konflik kapt
kapt {
    correctErrorTypes = true
}