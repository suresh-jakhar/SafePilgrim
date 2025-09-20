plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    // alias(libs.plugins.hilt) // Temporarily disabled until API layer is restored
}

android {
    namespace = "com.example.sp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sp"
        minSdk = 26
        targetSdk = 35  // Updated to Android 15 for 16 KB alignment support
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // NEW: API Configuration
        buildConfigField("String", "API_BASE_URL", "\"http://localhost:8080\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
        isCoreLibraryDesugaringEnabled = false
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    
    // Configure packaging options for 16 KB alignment
    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    // Add NDK build configuration for 16 KB alignment
    ndkVersion = "25.1.8937393"
    
    // Force 16 KB alignment by configuring splits
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
            isUniversalApk = false
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    
    // Phase 1: Digital ID & Blockchain
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    // ksp(libs.androidx.room.compiler) // Temporarily disabled
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)
    implementation(libs.androidx.biometric)
    implementation(libs.zxing.core)
    implementation(libs.zxing.android)
    
    // Phase 2: AI/ML & Enhanced Features
    implementation(libs.androidx.work.runtime)
    // implementation(libs.hilt.android) // Temporarily disabled until API layer is restored
    // ksp(libs.hilt.compiler) // Temporarily disabled until API layer is restored
    // implementation(libs.hilt.navigation.compose) // Temporarily disabled until API layer is restored
    
    // NEW: Network & API Communication
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)
    // ksp(libs.moshi.codegen) // Temporarily disabled
    implementation(libs.security.crypto)
    
    // NEW: Network State Management
    implementation("androidx.lifecycle:lifecycle-process:2.7.0")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}