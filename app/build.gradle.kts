plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "btl.skincareapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "btl.skincareapp"
        minSdk = 24
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val nav_version = "2.1.0-alpha02"
    implementation ("androidx.navigation:navigation-fragment:$nav_version") // For Kotlin use navigation-fragment-ktx
    implementation ("androidx.navigation:navigation-ui:$nav_version")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}