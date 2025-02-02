import com.github.gradle.node.npm.task.NpxTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.licensee)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.nodeGradle)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
        compilations["main"].packageJson {
//            customField(Pair("script", mapOf("postbuild" to "workbox generateSW kotlin/workbox-config.js")))
            customField(Pair("script", mapOf("postbuild" to "echo 'arisu' > arisu.txt")))
        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.navigation.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
}

tasks.register<NpxTask>("buildPWA") {
    dependsOn("clean", "npmInstall", "wasmJsBrowserDistribution")

    command = "workbox-cli"
    args = listOf("generateSW", "workbox-config.js")
}
