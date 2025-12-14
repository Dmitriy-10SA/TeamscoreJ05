plugins {
    id("java")
}

group = "ru.teamscore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //postgresql
    implementation("org.postgresql:postgresql:42.7.7")

    //gson
    implementation("com.google.code.gson:gson:2.8.9")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}