val aws_s3_sdk_version: String by project

dependencies {
    api(project(":application:aws:aws-contract"))

    implementation("aws.sdk.kotlin:s3:$aws_s3_sdk_version")
}
