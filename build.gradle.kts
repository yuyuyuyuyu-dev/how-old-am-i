import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinToolingSetupTask

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
    alias(libs.plugins.versionCatalogUpdate)
}

// The js and wasmJs targets each register their own npm install and tooling
// setup task on the root project, and nothing orders them against each other,
// so Gradle is free to run them at the same time. They all shell out to yarn,
// which serialises concurrent invocations behind one global mutex — that is the
// recurring "Waiting for the other yarn instance to finish (<pid>)" in CI logs.
// A loser of that race can return without having installed its workspace, and
// jsBrowserTest then fails with
//   Cannot find node module "kotlin-web-helpers/dist/kotlin-test-karma-runner.js"
// because build/js/packages/*-test was never populated. Letting yarn arbitrate
// is what makes this flaky, so hand every yarn-invoking task a build service
// that admits one user at a time and keep a single yarn process alive at once.
abstract class YarnSerializationService : BuildService<BuildServiceParameters.None>

val yarnSerialization =
    gradle.sharedServices.registerIfAbsent("yarnSerialization", YarnSerializationService::class) {
        maxParallelUsages.set(1)
    }

tasks.withType<KotlinNpmInstallTask>().configureEach { usesService(yarnSerialization) }
tasks.withType<KotlinToolingSetupTask>().configureEach { usesService(yarnSerialization) }

allprojects {
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint()
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }
    }
}

// Prettier handles the file types ktlint does not: Markdown, YAML, JSON, JS,
// CSS and HTML. It is configured once on the root project (rather than per
// subproject) so the whole repository is covered by a single Node/Prettier run,
// and it is left on Prettier's defaults so there is no house style to maintain.
spotless {
    format("prettier") {
        target(
            "**/*.md",
            "**/*.yml",
            "**/*.yaml",
            "**/*.json",
            "**/*.js",
            "**/*.css",
            "**/*.html"
        )
        targetExclude("**/build/**", "**/node_modules/**")
        prettier()
    }
}

detekt {
    config.setFrom(file("${project.rootDir}/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

// Gradle has no built-in formatter for version catalogs and Spotless does not
// cover them yet, so this plugin is what keeps libs.versions.toml diffs small
// and review-focused instead of leaving entry order to manual bikeshedding.
versionCatalogUpdate {
    // Deterministic ordering so unrelated dependency edits don't collide.
    sortByKey = true
}
