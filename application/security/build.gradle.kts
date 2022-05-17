val auth0_jwt_version: String by project
val codified_version: String by project

dependencies {
    api("com.auth0:java-jwt:$auth0_jwt_version")

    api(project(":application:security:security-contract"))
    implementation(project(":application:shared"))

    testFixturesImplementation("com.github.bright.codified:enums:$codified_version")
    testFixturesImplementation(testFixtures(project(":application:test-utils")))
}
