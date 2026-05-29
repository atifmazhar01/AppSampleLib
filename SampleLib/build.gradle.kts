plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish") // 1. Apply the maven-publish plugin
}

android {
    namespace = "com.example.samplelib"
    compileSdk = 36

    defaultConfig {
        minSdk = 25

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

// 2. Configure the publishing details
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                // Coordinates used to consume the library
                groupId = "com.example.samplelib"
                artifactId = "SampleLib"
                version = "1.0.0"

                // Applies the component created by the Android Gradle Plugin
                from(components["release"])
            }
        }

        // Define where the library will be built locally before pushing
        repositories {
            maven {
//                name = "LocalRepo"
//                url = uri(layout.buildDirectory.dir("outputs/maven-repo"))
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/atifmazhar01/AppSampleLib")   // ✅ your github username + repo name

                credentials {
                    username = providers.gradleProperty("gpr.user").orElse(System.getenv("USERNAME") ?: "").get()
                    password = providers.gradleProperty("gpr.key").orElse(System.getenv("TOKEN") ?: "").get()
                }
            }
        }
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}