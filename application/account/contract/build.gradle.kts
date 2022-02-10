dependencies {
    implementation(project(":application:mediator"))
    implementation(project(":application:shared"))

    testImplementation(testFixtures(project(":application:test-utils")))
}
