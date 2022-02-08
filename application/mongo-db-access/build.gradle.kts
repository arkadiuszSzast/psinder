val kmongo_version: String by project

dependencies {
    api("org.litote.kmongo:kmongo-coroutine-serialization:$kmongo_version")
    api("org.litote.kmongo:kmongo-id-serialization:$kmongo_version")

    implementation(project(":application:shared"))
}