import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "2.0.20-RC2"
    id("com.github.gmazzo.buildconfig") version "5.4.0"
}

group = "caddy"

buildConfig {
    buildConfigField("GIT_BRANCH", getCurrentBranch())
    buildConfigField("GIT_COMMIT", getLatestCommit())
    buildConfigField("GIT_REPO_URL", getRepoUrl())
    buildConfigField("GIT_LOCAL_COMMITS", hasLocalCommits())
    buildConfigField("GIT_LOCAL_CHANGES", hasLocalChanges())

}

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

    val ktormVersion = "4.1.0"

    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-support-sqlite:$ktormVersion")

    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
}

fun getCurrentRemote(): String? =
    exec("git", "remote")

fun getRepoUrl(): String? =
    exec("git", "remote", "get-url", getCurrentRemote() ?: "origin")?.removeSuffix(".git")

fun getCurrentBranch(): String? =
    exec("git", "symbolic-ref", "--short", "HEAD")

fun getLatestCommit(): String? =
    exec("git", "rev-parse", "--short", "HEAD")

fun hasLocalCommits(): Boolean {
    val branch = getCurrentBranch() ?: return false
    return exec("git", "log", "${getCurrentRemote()}/$branch..HEAD")?.isNotBlank() ?: false
}

fun hasLocalChanges(): Boolean =
    exec("git", "status", "-s")?.isNotEmpty() ?: false

fun exec(vararg command: String): String? {
    return try {
        val stdout = ByteArrayOutputStream()
        val errout = ByteArrayOutputStream()

        exec {
            commandLine = command.toList()
            standardOutput = stdout
            errorOutput = errout
            isIgnoreExitValue = true
        }

        if(errout.size() > 0)
            throw Error(errout.toString())

        stdout.toString().trim()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}