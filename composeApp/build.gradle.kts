import com.google.devtools.ksp.gradle.KspAATask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.aboutLibraries)
    alias(libs.plugins.ksp)
    alias(libs.plugins.composePwa)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport {
                        enabled.set(true)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.aboutlibraries.compose.m3)
            implementation(libs.kotlinInject.runtime)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            implementation(libs.yuyuyuyuyu.myComposables)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
    }
}

compose.desktop {
    application {
        mainClass = "dev.yuyuyuyuyu.howoldami.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.yuyuyuyuyu.howoldami"
            packageVersion = "1.0.0"
        }
    }
}

// Detekt is configured per-module here (not via the root `allprojects` block) because
// pointing it at the Kotlin Multiplatform source sets requires this module's `src` tree.
// Without this, the `detekt` task reports NO-SOURCE and analyzes nothing.
configure<DetektExtension> {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    source.setFrom("src")
}

dependencies {
    kspCommonMainMetadata(libs.kotlinInject.compiler)
}

tasks.withType<KspAATask>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

val composeMultiplatformVersion: String = libs.versions.composeMultiplatform.get()

configurations.configureEach {
    if (isCanBeResolved) {
        val pluginVersion = composeMultiplatformVersion
        incoming.afterResolve {
            val groupsBoundToPlugin =
                setOf(
                    "org.jetbrains.compose.runtime",
                    "org.jetbrains.compose.ui",
                    "org.jetbrains.compose.foundation",
                    "org.jetbrains.compose.animation",
                    "org.jetbrains.compose.material",
                    "org.jetbrains.compose.components"
                )
            val release = { version: String ->
                Regex("""^(\d+)\.(\d+)\.(\d+)""")
                    .find(version)
                    ?.destructured
                    ?.let { (major, minor, patch) ->
                        major.toLong() * 1_000_000 + minor.toLong() * 1_000 + patch.toLong()
                    }
                    ?: throw GradleException("Unrecognised Compose version $version")
            }
            val line = { version: String -> version.split(".").take(2).joinToString(".") }
            val pluginRelease = release(pluginVersion)
            val pluginLine = line(pluginVersion)
            val problems =
                resolutionResult.allComponents.mapNotNull {
                    it.moduleVersion
                }.mapNotNull { module ->
                    val coordinates = "${module.group}:${module.name}"
                    when {
                        module.group in groupsBoundToPlugin &&
                            release(module.version) > pluginRelease ->
                            "$coordinates resolved to ${module.version}, " +
                                "above Compose Multiplatform $pluginVersion"

                        module.group == "org.jetbrains.compose.material3" &&
                            line(module.version) != pluginLine ->
                            "$coordinates resolved to ${module.version}, " +
                                "outside the $pluginLine line of Compose Multiplatform $pluginVersion"

                        else -> null
                    }
                }
            if (problems.isNotEmpty()) {
                throw GradleException(problems.distinct().joinToString("\n"))
            }
        }
    }
}
