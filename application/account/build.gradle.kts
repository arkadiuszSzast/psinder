dependencies {
    api(project(":application:account:account-contract"))
    implementation(project(":application:security"))
    implementation(project(":application:shared"))
    implementation(project(":application:mongo-db-access"))
    implementation(project(":application:event-store"))
    implementation(project(":application:mediator"))
    implementation(project(":application:mail:mail-contract"))
    implementation(project(":application:feature-toggle"))

    testFixturesImplementation(testFixtures(project(":application:mongo-db-access")))
    testFixturesImplementation(testFixtures(project(":application:test-utils")))
    testFixturesImplementation(testFixtures(project(":application:event-store")))

    testImplementation(testFixtures(project(":application:mongo-db-access")))
    testImplementation(testFixtures(project(":application:event-store")))
    testImplementation(testFixtures(project(":application:test-utils")))
    testImplementation(testFixtures(project(":application:mediator")))
    testImplementation(testFixtures(project(":application:security")))
    testImplementation(testFixtures(project(":application:mail")))
}
