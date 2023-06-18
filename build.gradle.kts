plugins {
    id("java-library")
}

group = "org.reckenreb"
version = "0.8.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}