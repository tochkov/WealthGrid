plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.room)
}

android {
    namespace = "com.topdownedge.data"
    compileSdk = rootProject.ext["compileSdk"] as Int

    defaultConfig {
        minSdk = rootProject.ext["minSdk"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }
    
    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL_EODHD", "\"https://eodhd.com/api/\"")
            buildConfigField("String", "BASE_URL_EODHD_WSS", "\"wss://ws.eodhistoricaldata.com/ws/\"")
            isMinifyEnabled = false
        }
        release {
            buildConfigField("String", "BASE_URL_EODHD", "\"https://eodhd.com/api/\"")
            buildConfigField("String", "BASE_URL_EODHD_WSS", "\"wss://ws.eodhistoricaldata.com/ws\"")
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
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

kapt {
    // produce more accurate error messages
    correctErrorTypes = true
    arguments {
        // skip some validation steps during initialization
        arg("dagger.fastInit", "true")
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Networking
    implementation(libs.ktorfit.lib)
    implementation(libs.ktorfit.converters.response)
//    implementation(libs.ktorfit.converters.call)
//    implementation(libs.ktorfit.converters.flow)

    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}