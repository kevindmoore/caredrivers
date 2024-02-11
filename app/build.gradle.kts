plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  id("kotlin-parcelize")
  id("com.google.devtools.ksp")
  alias(libs.plugins.gradle.secrets)
  kotlin("plugin.serialization") version "1.9.10"
}

android {
  namespace = "com.mastertechsoftware.caredrivers"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.mastertechsoftware.caredrivers"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    buildConfig = true
    viewBinding = true
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "META-INF/LICENSE.txt"
      excludes += "META-INF/NOTICE.txt"
    }
  }
  secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
  }

}

dependencies {


  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.ktx)

  implementation(libs.constraintlayout)

  implementation(libs.json.serialization)

  implementation(libs.timber)

  implementation(libs.retrofit)
  implementation(libs.moshi)
  implementation(libs.loggingInterceptor)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.drawerlayout)
  implementation(libs.material)
  implementation(libs.play.services.maps)
  implementation(libs.androidx.appcompat)
  ksp (libs.moshiCodeGen)
  implementation(libs.retrofit.moshi.converter)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}