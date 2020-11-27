import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.31" apply true
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.31" apply true
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10"
}

group = "dev.alansantos"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Srping Data
    implementation("org.springframework.data:spring-data-jpa")

    // JWT
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // Swagger
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")

    // Postgres
    implementation("org.postgresql:postgresql:42.2.18")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring Boot Test/Dev
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:4.3.1")
    testImplementation("io.kotest:kotest-assertions-core:4.3.1")
    testImplementation("io.kotest:kotest-assertions-json-jvm:4.3.1")
    testImplementation("io.kotest:kotest-property:4.3.1")
    testImplementation("io.kotest:kotest-extensions-spring:4.3.1")

    // Mockk
    testImplementation("com.ninja-squad:springmockk:3.0.0")

    // H2
    testImplementation("com.h2database:h2")


}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
