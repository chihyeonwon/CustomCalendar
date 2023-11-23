import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.customcalendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.customcalendar"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "ENDPOINT_GET_HOLIDAY", gradleLocalProperties(rootDir).getProperty("endPoint.getHd"))
        buildConfigField("String", "API_KEY", gradleLocalProperties(rootDir).getProperty("api.key"))
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
    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.databinding:databinding-runtime:8.1.2")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx:20.2.1")
    implementation ("com.firebaseui:firebase-ui-storage:8.0.2")
    implementation ("com.google.android.material:material:1.6.1") // NavigationView

    // Firebase Cloud Messaging
    implementation ("com.google.firebase:firebase-messaging-ktx:23.2.1")
    implementation ("com.google.firebase:firebase-analytics-ktx:21.3.0")

    implementation ("com.google.code.gson:gson:2.8.7")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

}