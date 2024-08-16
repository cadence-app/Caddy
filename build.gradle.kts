import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.20-RC2"
}

group = "caddy"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "caddy.MainKt")
    }

    from(configurations.runtimeClasspath.get().map(::zipTree))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    val kordVersion = "0.14.0"

    implementation("dev.kord:kord-rest:$kordVersion")
    implementation("dev.kord:kord-gateway:$kordVersion")
    implementation("dev.kord:kord-core:$kordVersion")

    val ktorVersion = "2.3.11"

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    val argParserVersion = "2.0.7"

    implementation("com.xenomachina:kotlin-argparser:$argParserVersion")
}