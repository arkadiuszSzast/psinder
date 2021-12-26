import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val ktor_version: String by project
val kotlin_version: String by project
val koin_version: String by project
val pipelinr_version: String by project
val logback_version: String by project
val jbcrypt_version: String by project
val kmongo_version: String by project
val event_store_db_version: String by project
val tcnative_version: String by project
val kotest_version: String by project
val strikt_version: String by project
val arrow_version: String by project
val arrow_jackson_version: String by project
val kotlin_logging_version: String by project
val micrometer_dd_version: String by project
val janino_version: String by project
val logstash_logback_encoder_version: String by project
val ktor_opentracing_version: String by project
val dd_tracer_version: String by project
val sentry_version: String by project
val mockk_version: String by project

val tcnative_classifier: String = with(System.getProperty("os.name").toLowerCase()) {
    when {
        this.contains("win") -> "windows-x86_64"
        this.contains("linux") -> "linux-x86_64"
        this.contains("mac") -> "osx-x86_64"
        else -> ""
    }
}

plugins {
    application
    jacoco
    id("org.sonarqube") version "3.3"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.gitlab.arturbosch.detekt").version("1.19.0")
    kotlin("jvm") version "1.6.0"
}

group = "com"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

sonarqube {
    properties {
        property("sonar.projectKey", "arkadiuszSzast_psinder")
        property("sonar.organization", "arkadiuszszast")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_LOGIN_TOKEN"))
        property("sonar.kotlin.detekt.reportPaths", "${project.buildDir}/reports/detekt/detekt.xml")
        property(
            "sonar.kotlin.ktlint.reportPaths",
            arrayOf(
                "${project.buildDir}/reports/ktlint/ktlintKotlinScriptCheck/ktlintKotlinScriptCheck.json",
                "${project.buildDir}/reports/ktlint/ktlintMainSourceSetCheck/ktlintMainSourceSetCheck.json",
                "${project.buildDir}/reports/ktlint/ktlintTestSourceSetCheck/ktlintTestSourceSetCheck.json"
            ).joinToString(",")
        )
    }
}

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

detekt {
    buildUponDefaultConfig = true
    allRules = false
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-integrations-jackson-module:$arrow_jackson_version")
    implementation("com.github.arkadiuszSzast:pipelinr-fork:$pipelinr_version")
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("com.github.traxterz:ktor-plugin-event-store-db:$event_store_db_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlin_logging_version")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstash_logback_encoder_version")
    implementation("io.netty:netty-tcnative:$tcnative_version")
    implementation("io.netty:netty-tcnative-boringssl-static:$tcnative_version")
    implementation("io.netty:netty-tcnative-boringssl-static:$tcnative_version:$tcnative_classifier")
    implementation("org.mindrot:jbcrypt:$jbcrypt_version")
    implementation("com.zopa:ktor-opentracing:$ktor_opentracing_version")
    implementation("io.micrometer:micrometer-registry-datadog:$micrometer_dd_version")
    implementation("com.datadoghq:dd-trace-api:$dd_tracer_version")
    implementation("com.datadoghq:dd-trace-ot:$dd_tracer_version")
    implementation("io.sentry:sentry:$sentry_version")
    implementation("io.sentry:sentry-kotlin-extensions:$sentry_version")
    implementation("org.codehaus.janino:janino:$janino_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.strikt:strikt-core:$strikt_version")
    testImplementation("io.strikt:strikt-arrow:$strikt_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")

    implementation(platform("io.arrow-kt:arrow-stack:$arrow_version"))
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
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
