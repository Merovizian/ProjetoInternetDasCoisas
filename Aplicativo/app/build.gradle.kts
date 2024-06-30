plugins {
}

    android {
    namespace = "com.example.casainteligente"
    compileSdk = 34

    defaultConfig {
      applicationId = "com.example.casainteligente"
    minSdk = 19
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
       release {
           isMinifyEnabled = false
           proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
       }
    }
    }

  dependencies {

  }