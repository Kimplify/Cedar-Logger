plugins {
    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.android.multiplatform.library).apply(false)
    alias(libs.plugins.maven.publish).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.kotlinNativeCocoaPods).apply(false)
    alias(libs.plugins.dokka).apply(false)
    alias(libs.plugins.kover).apply(false)
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

apiValidation {
    ignoredProjects.addAll(listOf("sample", "androidApp"))
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**", "sample/**", "androidApp/**")
        ktlint("1.5.0").editorConfigOverride(
            mapOf(
                "ktlint_standard_backing-property-naming" to "disabled"
            )
        )
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint("1.5.0")
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    source.setFrom(
        "cedar-logging/src/commonMain/kotlin",
        "cedar-logging/src/androidMain/kotlin",
        "cedar-logging/src/iosMain/kotlin",
        "cedar-logging/src/jvmMain/kotlin",
        "cedar-logging/src/jsMain/kotlin",
        "cedar-logging/src/wasmJsMain/kotlin"
    )
}
