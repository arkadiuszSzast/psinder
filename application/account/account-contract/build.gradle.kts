dependencies {
    implementation(project(":application:mediator"))
    implementation(project(":application:shared"))
    implementation(project(":application:security:security-contract"))
    implementation(project(":application:event-store:event-store-contract"))

    testImplementation(testFixtures(project(":application:test-utils")))
}
