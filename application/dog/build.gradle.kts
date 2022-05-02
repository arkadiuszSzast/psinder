dependencies {
    api(project(":application:dog:dog-contract"))

    implementation(project(":application:mediator"))
    implementation(project(":application:mongo-db-access"))
    implementation(project(":application:shared"))
}
