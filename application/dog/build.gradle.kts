dependencies {
    api(project(":application:dog:dog-contract"))

    implementation(project(":application:mediator"))
    implementation(project(":application:mongo-db-access"))
    implementation(project(":application:shared"))
    implementation(project(":application:file-storage:file-storage-contract"))
}
