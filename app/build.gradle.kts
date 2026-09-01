import com.android.build.api.variant.BuildConfigField

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.first_non_interesting_username.projects_list"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "io.github.first_non_interesting_username.projects_list"
        minSdk = 24
        targetSdk = 36
        versionCode = 10002
        versionName = "1.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

androidComponents {
    onVariants { variant ->
        val projectRootDir = rootDir

        val gitHashProvider = providers.of(GitHashValueSource::class) {
            parameters {
                rootDir.set(projectRootDir)
            }
        }

        variant.buildConfigFields?.put(
            "GIT_HASH",
            gitHashProvider.map { hash ->
                BuildConfigField(
                    type = "String",
                    value = "\"$hash\"",
                    comment = "Git commit hash"
                )
            }
        )
    }
}


dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.material)
    implementation(libs.navigation.compose)
    implementation(libs.aboutlibraries.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

abstract class GitHashValueSource : ValueSource<String, GitHashValueSource.Params> {
    interface Params : ValueSourceParameters {
        val rootDir: DirectoryProperty
    }

    override fun obtain(): String {
        return try {
            val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
                .directory(parameters.rootDir.get().asFile)
                .redirectErrorStream(true)
                .start()
            val result = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            if (process.exitValue() == 0) result else "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }
}
