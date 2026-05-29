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
                groupId = "com.example.samplelib"
                artifactId = "app-sample-lib"
                version = "1.0.0"

                from(components["release"])

                // Add this POM block to explicitly link the artifact to your GitHub repo
                pom {
                    name.set("SampleLib")
                    description.set("An Android sample library")
                    url.set("https://github.com/atifmazhar01/AppSampleLib")

                    scm {
                        connection.set("scm:git:github.com/atifmazhar01/AppSampleLib.git")
                        developerConnection.set("scm:git:ssh://github.com/atifmazhar01/AppSampleLib.git")
                        url.set("https://github.com/atifmazhar01/AppSampleLib")
                    }
                }
            }
//            register<MavenPublication>("release") {
//                // Coordinates used to consume the library
//                groupId = "com.example.samplelib"
//                artifactId = "SampleLib"
//                version = "1.0.0"
//
//                // Applies the component created by the Android Gradle Plugin
//                from(components["release"])
//            }
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