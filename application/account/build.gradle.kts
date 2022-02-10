dependencies {
    implementation(project(":application:shared"))
    implementation(project(":application:jwt"))
    implementation(project(":application:mongo-db-access"))
    implementation(project(":application:event-store"))
    implementation(project(":application:mediator"))
    implementation(project(":application:account:contract"))
}
