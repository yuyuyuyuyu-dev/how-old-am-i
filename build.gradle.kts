import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinToolingSetupTask

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.versionCatalogUpdate)
}

// The npm install and tooling setup tasks land on the root project, and nothing
// orders them against each other, so Gradle is free to run them at the same
// time. They all shell out to yarn, which serialises concurrent invocations
// behind one global mutex — that is the recurring "Waiting for the other yarn
// instance to finish (<pid>)" in CI logs. A loser of that race can return
// without having installed its workspace, and the browser test task then fails
// with
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

ktlint {
    version = libs.versions.ktlintCli
}

versionCatalogUpdate {
    sortByKey = true
}
