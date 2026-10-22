import com.google.devtools.ksp.gradle.KspAATask
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.aboutLibraries)
    alias(libs.plugins.ksp)
    alias(libs.plugins.composePwa)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

ktlint {
    version = libs.versions.ktlintCli
    filter {
        val buildPath =
            layout.buildDirectory
                .get()
                .asFile.path
        exclude { it.file.path.startsWith(buildPath) }
    }
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
        compilerOptions {
            freeCompilerArgs.add("-Xpartial-linkage-loglevel=ERROR")
        }
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

aboutLibraries {
    export {
        outputFile = file("src/commonMain/composeResources/files/aboutlibraries.json")
    }
}

compose.resources {
    customDirectory(
        sourceSetName = "commonMain",
        directoryProvider =
            tasks
                .named("exportLibraryDefinitions")
                .map { layout.projectDirectory.dir("src/commonMain/composeResources") },
    )
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

detekt {
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

tasks.withType<BaseKtLintCheckTask>().configureEach {
    mustRunAfter("kspCommonMainKotlinMetadata")
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
            val rank = { version: String ->
                Regex("""^(\d+)\.(\d+)\.(\d+)(?:-(alpha|beta|rc)(\d+))?$""")
                    .matchEntire(version)
                    ?.destructured
                    ?.let { (major, minor, patch, stage, number) ->
                        val release =
                            (major.toLong() * 1_000 + minor.toLong()) * 1_000 + patch.toLong()
                        val stageRank = listOf("alpha", "beta", "rc", "").indexOf(stage).toLong()
                        (release * 10 + stageRank) * 1_000 + (number.toLongOrNull() ?: 0)
                    }
                    ?: throw GradleException("Unrecognised Compose version $version")
            }
            val pluginRank = rank(pluginVersion)
            val problems =
                resolutionResult.allComponents
                    .mapNotNull { it.moduleVersion }
                    .filter { it.group in groupsBoundToPlugin && rank(it.version) > pluginRank }
                    .map {
                        "${it.group}:${it.name} resolved to ${it.version}, " +
                            "above Compose Multiplatform $pluginVersion"
                    }
            if (problems.isNotEmpty()) {
                throw GradleException(problems.distinct().joinToString("\n"))
            }
        }
    }
}
