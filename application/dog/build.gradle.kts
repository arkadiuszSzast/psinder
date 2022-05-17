dependencies {
    api(project(":application:dog:dog-contract"))

    implementation(project(":application:mediator"))
    implementation(project(":application:mongo-db-access"))
    implementation(project(":application:security"))
    implementation(project(":application:shared"))
    implementation(project(":application:file-storage:file-storage-contract"))
    implementation(project(":application:event-store"))

    testFixturesImplementation(testFixtures(project(":application:test-utils")))
    testFixturesImplementation(testFixtures(project(":application:mongo-db-access")))

    testImplementation(testFixtures(project(":application:test-utils")))
    testImplementation(testFixtures(project(":application:security")))
    testImplementation(testFixtures(project(":application:event-store")))
    testImplementation(testFixtures(project(":application:mediator")))
    testImplementation(testFixtures(project(":application:mongo-db-access")))
}
