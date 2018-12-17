import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    `java-library`
    jacoco
    `build-dashboard`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:26.0-jre")
    testImplementation("junit:junit:4.12")
}

tasks {
    withType<Test> {
        testLogging.events = setOf(PASSED, SKIPPED, FAILED, STANDARD_OUT, STANDARD_ERROR)
        testLogging.showStandardStreams = true
    }
}

val jacocoTestReport by tasks.named("jacocoTestReport")
jacocoTestReport.dependsOn(tasks.named("test"))
