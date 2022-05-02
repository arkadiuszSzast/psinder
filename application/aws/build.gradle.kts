val aws_s3_sdk_version: String by project
val tika_version: String by project

dependencies {
    api(project(":application:aws:aws-contract"))
    implementation(project(":application:shared"))
    implementation("aws.sdk.kotlin:s3:$aws_s3_sdk_version")
    implementation("org.apache.tika:tika-core:$tika_version")
}
