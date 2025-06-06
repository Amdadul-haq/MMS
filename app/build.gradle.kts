plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mosque_management_system"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mosque_management_system"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.material.v1110)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core )
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.gridlayout)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.viewpager2)
    implementation(libs.dotsindicator) // For dots indicator
    implementation(libs.lottie)


}