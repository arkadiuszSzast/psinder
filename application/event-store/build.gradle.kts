val event_store_db_version: String by project
val kmongo_version: String by project

dependencies {
    api("com.github.arkadiuszSzast:ktor-plugin-event-store-db:$event_store_db_version")

    implementation(project(":application:mediator"))
    implementation(project(":application:shared"))

    testImplementation(testFixtures(project(":application:test-utils")))
}
