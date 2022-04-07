import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val ktor_version: String by project
val kotlin_version: String by project
val koin_version: String by project
val kmongo_version: String by project
val logback_version: String by project
val jbcrypt_version: String by project
val kotest_version: String by project
val strikt_version: String by project
val arrow_version: String by project
val kotlin_datetime_version: String by project
val kotlin_logging_version: String by project
val janino_version: String by project
val logstash_logback_encoder_version: String by project
val mockk_version: String by project
val konform_version: String by project
val codified_version: String by project
val faker_version: String by project
val global_requestdata_version: String by project

val ktlint_scripts_reports =
    project.allprojects.map { "${it.buildDir}/reports/ktlint/ktlintKotlinScriptCheck/ktlintKotlinScriptCheck.json" }
val ktlint_sources_reports =
    project.allprojects.map { "${it.buildDir}/reports/ktlint/ktlintMainSourceSetCheck/ktlintMainSourceSetCheck.json" }
val ktlint_tests_reports =
    project.allprojects.map { "${it.buildDir}/reports/ktlint/ktlintTestSourceSetCheck/ktlintTestSourceSetCheck.json" }

plugins {
    jacoco
    `java-test-fixtures`
    id("org.sonarqube") version "3.3"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.gitlab.arturbosch.detekt").version("1.19.0")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    kotlin("jvm") version "1.6.0"
    kotlin("kapt") version "1.6.10"
}
allprojects {
    group = "com.psinder"
    version = "0.0.1"
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    apply(plugin = "kotlin")
    apply(plugin = "jacoco")
}

task<Detekt>("detektAll") {
    parallel = true
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    config.setFrom(files("${project.rootProject.projectDir}/config/detekt/detekt.yml"))
    buildUponDefaultConfig = false
}

task<Detekt>("detektAllSneaky") {
    parallel = true
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    config.setFrom(files("${project.rootProject.projectDir}/config/detekt/detekt.yml"))
    buildUponDefaultConfig = false
    ignoreFailures = true
}

task<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.withType<Test>() })
    dependsOn(subprojects.map { it.tasks.withType<JacocoReport>() })
    additionalSourceDirs.setFrom(subprojects.map { it.the<SourceSetContainer>()["main"].allSource.srcDirs })
    sourceDirectories.setFrom(subprojects.map { it.the<SourceSetContainer>()["main"].allSource.srcDirs })
    classDirectories.setFrom(subprojects.map { it.the<SourceSetContainer>()["main"].output })
    executionData.setFrom(
        project.fileTree(".") {
            include("**/build/jacoco/test.exec")
        }
    )
    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/reports/jacoco/html"))
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "arkadiuszSzast_psinder")
        property("sonar.organization", "arkadiuszszast")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_LOGIN_TOKEN"))
        property("sonar.kotlin.detekt.reportPaths", "${project.buildDir}/reports/detekt/detekt.xml")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project.buildDir}/reports/jacoco/jacocoRootReport/jacocoRootReport.xml"
        )
        property(
            "sonar.kotlin.ktlint.reportPaths",
            arrayOf(
                ktlint_scripts_reports,
                ktlint_sources_reports,
                ktlint_tests_reports
            ).flatMap { it }.joinToString(",")
        )
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.gradle.java-test-fixtures")
    apply(plugin = "kotlin-kapt")

    ktlint {
        verbose.set(true)
        outputToConsole.set(true)
        coloredOutput.set(true)
        reporters {
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.JSON)
            reporter(ReporterType.HTML)
        }
    }

    dependencies {
        kapt("io.arrow-kt:arrow-meta:1.0.1")
        implementation("io.ktor:ktor-auth-jwt:$ktor_version")
        implementation("io.ktor:ktor-serialization:$ktor_version")
        implementation("io.insert-koin:koin-ktor:$koin_version")
        implementation("org.litote.kmongo:kmongo-id:$kmongo_version")
        implementation("io.arrow-kt:arrow-core")
        implementation("io.arrow-kt:arrow-optics")
        implementation("com.github.bright.codified:enums:$codified_version")
        implementation("com.github.bright.codified:enums-serializer:$codified_version")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlin_datetime_version")
        implementation("io.konform:konform-jvm:$konform_version")
        implementation("com.github.arkadiuszSzast:ktor-global-request-data:$global_requestdata_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("io.github.microutils:kotlin-logging-jvm:$kotlin_logging_version")
        implementation("net.logstash.logback:logstash-logback-encoder:$logstash_logback_encoder_version")

        implementation("org.mindrot:jbcrypt:$jbcrypt_version")

        implementation("org.codehaus.janino:janino:$janino_version")

        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
        testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
        testImplementation("io.strikt:strikt-core:$strikt_version")
        testImplementation("io.strikt:strikt-arrow:$strikt_version")
        testImplementation("io.mockk:mockk:$mockk_version")
        testImplementation("io.insert-koin:koin-test:$koin_version")

        implementation(platform("io.arrow-kt:arrow-stack:$arrow_version"))
    }

    tasks {
        withType<Detekt> {
            parallel = true
            config.setFrom(files("${project.rootProject.projectDir}/config/detekt/detekt.yml"))
            buildUponDefaultConfig = false
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        reports {
            xml.required.set(true)
            csv.required.set(true)
            html.required.set(true)
        }
    }

    kotlin.target.compilations["test"].associateWith(kotlin.target.compilations["main"])
    kotlin.target.compilations["testFixtures"].associateWith(kotlin.target.compilations["main"])
}
