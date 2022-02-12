dependencies {
    implementation(project(":application:shared"))
    implementation(project(":application:jwt"))
    implementation(project(":application:mongo-db-access"))
    implementation(project(":application:event-store"))
    implementation(project(":application:mediator"))
    implementation(project(":application:account:contract"))

    testFixturesImplementation(testFixtures(project(":application:mongo-db-access")))
    testFixturesImplementation(testFixtures(project(":application:test-utils")))

    testImplementation(testFixtures(project(":application:mongo-db-access")))
    testImplementation(testFixtures(project(":application:test-utils")))
    testImplementation(testFixtures(project(":application:mediator")))
}
