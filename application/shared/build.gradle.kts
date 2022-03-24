val auth0_jwt_version: String by project
val kmongo_version: String by project

dependencies {
    implementation("com.auth0:java-jwt:$auth0_jwt_version")
    implementation("org.litote.kmongo:kmongo-id-serialization:$kmongo_version")
}
