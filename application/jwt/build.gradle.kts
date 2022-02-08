val auth0_jwt_version: String by project

dependencies {
    api("com.auth0:java-jwt:${auth0_jwt_version}")

    implementation(project(":application:shared"))
}