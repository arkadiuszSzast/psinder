val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val jbcrypt_version: String by project
val kmongo_version: String by project
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
    kotlin("jvm") version "1.6.0"
}

group = "com"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
//    applicationDefaultJvmArgs = listOf("-javaagent:/dd-java-agent.jar")
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
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("io.arrow-kt:arrow-core:$arrow_version")
    implementation("io.arrow-kt:arrow-integrations-jackson-module:$arrow_jackson_version")
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.netty:netty-tcnative:$tcnative_version")
    implementation("io.netty:netty-tcnative-boringssl-static:$tcnative_version")
    implementation("io.netty:netty-tcnative-boringssl-static:$tcnative_version:$tcnative_classifier")
    implementation("org.mindrot:jbcrypt:$jbcrypt_version")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlin_logging_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-datadog:$micrometer_dd_version")
    implementation("org.codehaus.janino:janino:$janino_version")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstash_logback_encoder_version")
    implementation("com.zopa:ktor-opentracing:$ktor_opentracing_version")
    implementation("com.datadoghq:dd-trace-api:$dd_tracer_version")
    implementation("com.datadoghq:dd-trace-ot:$dd_tracer_version")
    implementation("io.sentry:sentry:$sentry_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.strikt:strikt-core:$strikt_version")
    testImplementation("io.strikt:strikt-arrow:$strikt_version")
    testImplementation("io.mockk:mockk:$mockk_version")

    implementation(platform("io.arrow-kt:arrow-stack:$arrow_version"))
}

val downloadDDJavaAgent = tasks.register<de.undercouch.gradle.tasks.download.Download>("downloadDDJavaAgent") {
    src("https://dtdg.co/latest-java-tracer")
    dest(file("build/libs/dd-java-agent.jar"))
    overwrite(false)
}

tasks.named("build") {
    dependsOn(downloadDDJavaAgent)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
