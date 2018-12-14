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
    implementation("org.slf4j:slf4j-api:1.7.25")

    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.zeromq:jeromq:0.4.3")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0")

    testImplementation("junit:junit:4.12")
    testImplementation("net.jodah:concurrentunit:0.4.3")

    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
}

val jacocoTestReport by tasks.named("jacocoTestReport")
jacocoTestReport.dependsOn(tasks.named("test"))

val buildDashboard by tasks.named("buildDashboard")
buildDashboard.dependsOn(jacocoTestReport)
