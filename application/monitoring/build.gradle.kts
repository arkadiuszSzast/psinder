val ktor_version: String by project
val micrometer_dd_version: String by project
val ktor_opentracing_version: String by project
val dd_tracer_version: String by project
val sentry_version: String by project

dependencies {
    implementation("com.zopa:ktor-opentracing:$ktor_opentracing_version")
    implementation("io.micrometer:micrometer-registry-datadog:$micrometer_dd_version")
    implementation("com.datadoghq:dd-trace-api:$dd_tracer_version")
    implementation("com.datadoghq:dd-trace-ot:$dd_tracer_version")
    implementation("io.sentry:sentry:$sentry_version")
    implementation("io.sentry:sentry-kotlin-extensions:$sentry_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")

    implementation(project(":application:mediator"))
    implementation(project(":application:shared"))

    testImplementation(testFixtures(project(":application:test-utils")))
}
