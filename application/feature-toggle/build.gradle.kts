val ktor_version: String by project
val config_cat_version: String by project

dependencies {
    implementation("com.configcat:configcat-java-client:$config_cat_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")

    api(project(":application:feature-toggle:feature-toggle-contract"))
    implementation(project(":application:shared"))
    implementation(project(":application:mediator"))
}
