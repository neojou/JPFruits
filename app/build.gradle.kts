plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"  // 新增：Compose Compiler Plugin，版本匹配 Kotlin
}

android {
    namespace = "com.neojou.jpfruits"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.neojou.jpfruits"
        minSdk = 28
        targetSdk = 35
        versionCode = 9
        versionName = "2.03"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // Enables code-related app optimization
            isMinifyEnabled = true

            // Enables resource shrinking
            isShrinkResources = true

            proguardFiles(
                // Default file with automatically generated optimization rules.
                getDefaultProguardFile("proguard-android-optimize.txt"),
            )

            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"  // 或 'FULL' 以獲得更多細節
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11  // 更新：從 1_8 升級到 11
        targetCompatibility = JavaVersion.VERSION_11  // 更新：從 1_8 升級到 11
    }
    kotlinOptions {
        jvmTarget = "11"  // 更新：從 "1.8" 升級到 "11"
    }
    buildFeatures {
        compose = true
        dataBinding = true
    }
    // 移除：composeOptions 區塊（舊版設定，Compose Compiler Plugin 會自動處理）
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// 新增：配置所有 JavaCompile tasks 以顯示詳細的棄用警告
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

dependencies {
    val fragment_version = "1.8.4"  // 更新：從 1.6.2 升級到最新穩定版
    // Java language implementation
    implementation("androidx.fragment:fragment:$fragment_version")
    // Kotlin
    implementation("androidx.fragment:fragment-ktx:$fragment_version")

    implementation("androidx.core:core-ktx:1.15.0")  // 更新：從 1.12.0 升級
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")  // 更新：從 2.7.0 升級
    implementation("androidx.activity:activity-compose:1.9.3")  // 更新：從 1.8.2 升級
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))  // 更新：從 2024.02.02 升級到最新
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.7.0")  // 更新：從 1.6.1 升級
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")  // 更新：從 2.1.4 升級
    implementation("androidx.databinding:databinding-runtime:8.5.2")  // 更新：從 8.3.0 升級
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")  // 更新：從 1.1.5 升級
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")  // 更新：從 3.5.1 升級
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))  // 更新 BOM
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
