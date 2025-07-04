import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
}

val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretsProperties = Properties()
secretsProperties.load(FileInputStream(secretsPropertiesFile))

android {
    namespace = "com.example.eurogymclass"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.eurogymclass"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "CHATBOT_API_KEY", "\"${secretsProperties["OPENAI_API_KEY"]}\"")

        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
    implementation((libs.firebase.firestore))
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.navigation.compose)

    implementation("com.google.firebase:firebase-messaging:23.0.0")

    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("com.google.android.gms:play-services-auth:20.2.0")

    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.transition)
    implementation(libs.androidx.espresso.core)
    implementation(libs.ads.mobile.sdk)
    implementation(libs.firebase.dataconnect)
    implementation(libs.transport.api)
    implementation(libs.transport.api)
    implementation(libs.transport.api)
    implementation(libs.transport.api)
    implementation(libs.transport.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}