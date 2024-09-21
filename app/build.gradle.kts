plugins {
    alias(libs.plugins.android.application) // Plugin alias from your version catalog
    alias(libs.plugins.jetbrains.kotlin.android) // Plugin alias from your version catalog
}

android {
    namespace = "com.example.icetask4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.icetask4"
        minSdk = 27
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) // Proper use of 'implementation'
    implementation(libs.androidx.appcompat) // Proper use of 'implementation'
    implementation(libs.material) // Proper use of 'implementation'
    implementation(libs.androidx.activity) // Proper use of 'implementation'
    implementation(libs.androidx.constraintlayout) // Proper use of 'implementation'
    implementation(libs.play.services.location) // Proper use of 'implementation'

    // Google Maps dependency for map functionality
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    testImplementation(libs.junit) // Correct placement
    androidTestImplementation(libs.androidx.junit) // Correct placement
    androidTestImplementation(libs.androidx.espresso.core) // Correct placement
}
