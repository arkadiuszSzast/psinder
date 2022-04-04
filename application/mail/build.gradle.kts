val sendgrid_version: String by project

dependencies {
    api("com.sendgrid:sendgrid-java:$sendgrid_version")

    api(project(":application:mail:mail-contract"))
    implementation(project(":application:shared"))
    implementation(project(":application:mediator"))
    implementation(project(":application:event-store"))
    implementation(project(":application:security:security-contract"))

    testFixturesImplementation(testFixtures(project(":application:test-utils")))

    testImplementation(testFixtures(project(":application:test-utils")))
    testImplementation(testFixtures(project(":application:event-store")))
    testImplementation(testFixtures(project(":application:security")))
}
