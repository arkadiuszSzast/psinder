val ktor_version: String by project
val koin_version: String by project
val kotest_version: String by project
val tika_version: String by project
val aws_s3_sdk_version: String by project
val test_containers_version: String by project

dependencies {
    api(project(":application:aws:aws-contract"))

    implementation(project(":application:file-storage:file-storage-contract"))
    implementation(project(":application:shared"))
    implementation("aws.sdk.kotlin:s3:$aws_s3_sdk_version")
    implementation("org.apache.tika:tika-core:$tika_version")

    testImplementation(testFixtures(project(":application:test-utils")))
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")

    testFixturesImplementation("org.testcontainers:testcontainers:$test_containers_version")
    testFixturesImplementation("io.insert-koin:koin-test:$koin_version")
    testFixturesImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
}
