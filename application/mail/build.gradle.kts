val sendgrid_version: String by project

dependencies {
    api("com.sendgrid:sendgrid-java:$sendgrid_version")

    implementation(project(":application:shared"))
}
