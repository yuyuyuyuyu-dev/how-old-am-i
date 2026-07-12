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
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.composePwa)
}

kotlin {
    // The custom .dependsOn() wiring below (jvmWasmJsTest) stops Kotlin from applying
    // the default hierarchy template automatically, which silently drops the webMain
    // source set — and with it src/webMain/resources (index.html, the PWA assets) and
    // the web Main.kt. Apply the template explicitly so webMain keeps existing.
    applyDefaultHierarchyTemplate()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    js {
        browser()
        binaries.executable()
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
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.yuyuyuyuyu.myMaterialTheme)
            implementation(libs.yuyuyuyuyu.siimpleTopAppBar)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        // Compose UI tests (runComposeUiTest) only run where the Skiko-backed test
        // harness exists: jvm (desktop) and wasmJs. They fail on the js (ReferenceError)
        // and Android-unit-test targets, so they cannot live in commonTest. This shared
        // source set runs them on jvm locally (fast) and wasmJs in CI (production parity).
        val jvmWasmJsTest by creating
        jvmWasmJsTest.dependsOn(commonTest.get())
        jvmTest.get().dependsOn(jvmWasmJsTest)
        wasmJsTest.get().dependsOn(jvmWasmJsTest)
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
