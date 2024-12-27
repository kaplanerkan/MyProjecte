plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.navigation.safeargs.kotlin)

    id ("kotlin-parcelize")


}

android {
    namespace = "com.kiosk.mysimpletodo"
    compileSdk = 35

    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.kiosk.mysimpletodo"
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
        buildConfig = true
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
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity)
    implementation(libs.constraintlayout)


    // Activity & Fragment ktx
    implementation (libs.androidx.activity.ktx)




    // Room
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
   // implementation (libs.hilt.lifecycle.viewmodel)


    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.5")


    // Glide
    implementation(libs.glide)

    // timber logging
    implementation (libs.timber)


    // Livecycle-ktx
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.extensions)

    // Navigation
    implementation (libs.navigation.fragment)
    implementation (libs.navigation.ui)
    implementation(libs.fragment.ktx)

    // coroutines
    implementation (libs.kotlinx.coroutines.android)



}
