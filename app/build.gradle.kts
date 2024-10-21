plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.pidevv1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pidevv1"
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
}

dependencies {
    implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-compiler:2.6.1")
    implementation(libs.security.crypto)
    implementation ("com.auth0.android:jwtdecode:2.0.0")
    implementation(libs.room.common)
    implementation("com.auth0:java-jwt:4.2.1")
implementation("org.mindrot:jbcrypt:0.4")

            implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.identity.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}