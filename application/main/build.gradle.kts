val ktor_version: String by project
val tcnative_version: String by project

val tcnative_classifier: String = with(System.getProperty("os.name").toLowerCase()) {
    when {
        this.contains("win") -> "windows-x86_64"
        this.contains("linux") -> "linux-x86_64"
        this.contains("mac") -> "osx-x86_64"
        else -> ""
    }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

plugins {
    application
}
dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("io.netty:netty-tcnative:$tcnative_version")
    implementation("io.netty:netty-tcnative-boringssl-static:$tcnative_version")
    implementation("io.netty:netty-tcnative-boringssl-static:$tcnative_version:$tcnative_classifier")

    implementation(project(":application:shared"))
    implementation(project(":application:security"))
    implementation(project(":application:mediator"))
    implementation(project(":application:monitoring"))
    implementation(project(":application:event-store"))
    implementation(project(":application:mongo-db-access"))
    implementation(project(":application:account"))
    implementation(project(":application:account:account-contract"))

    testImplementation(testFixtures(project(":application:test-utils")))
}
