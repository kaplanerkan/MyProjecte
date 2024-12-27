plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.kiosk.mysimplelivedatamitviewmodel"
    compileSdk = 35

    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.kiosk.mysimplelivedatamitviewmodel"
        minSdk = 24

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)



    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.fragment.ktx)



    // ViewModel
    implementation (libs.lifecycle.viewmodel.ktx)
    // LiveData
    implementation (libs.lifecycle.livedata.ktx)
    // Lifecycles only (without ViewModel or LiveData)
    //implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation(libs.lifecycle.runtime.ktx)

}
