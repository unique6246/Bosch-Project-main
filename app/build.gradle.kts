plugins {
    id("com.android.application") // Plugin for Android application module
    id("org.jetbrains.kotlin.android") // Plugin for Kotlin support in Android
}

android {
    namespace = "com.example.myapplication" // Unique application identifier
    compileSdk = 34 // Compile against Android SDK version 34
    sourceSets {
        getByName("main").assets.srcDirs("src/main/assets") // Specify asset directory location
    }

    defaultConfig {
        applicationId = "com.example.myapplication" // Application ID 
        minSdk = 24 // Minimum Android SDK version supported
        targetSdk = 34 // Target Android SDK version
        versionCode = 1 // Application version code
        versionName = "1.0" // Application version name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner for instrumentation tests
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Disable code shrinking for release build
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") // ProGuard configuration files
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Java source compatibility version
        targetCompatibility = JavaVersion.VERSION_1_8 // Java target compatibility version
    }
    kotlinOptions {
        jvmTarget = "1.8" // JVM target version for Kotlin
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1") // Android KTX core library for Kotlin
    implementation("androidx.appcompat:appcompat:1.7.0") // Support a library for backward-compatible features
    implementation("com.google.android.material:material:1.12.0") // Material Design components
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // ConstraintLayout for flexible UI design

    implementation ("com.google.ar:core:1.44.0") // ARCore library for augmented reality
    implementation("com.google.ar.sceneform.ux:sceneform-ux:1.17.1") // Scene form UX library for AR
    implementation("com.google.ar.sceneform:assets:1.17.1")
    implementation("androidx.gridlayout:gridlayout:1.0.0") // Scene form assets library for AR

    testImplementation("junit:junit:4.13.2") // JUnit for unit testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // AndroidX JUnit extension for Android tests
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Espresso for UI testing
}