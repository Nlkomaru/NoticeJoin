plugins {
    id("java")
    id("eclipse")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.0.1"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.noticemc"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.0.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.1")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("net.kyori:adventure-api:4.10.0")
    implementation("net.kyori:adventure-text-minimessage:4.10.0")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("net.dv8tion:JDA:5.0.0-alpha.9")
}
java {
    val version = JavaVersion.VERSION_17
    sourceCompatibility = version
    targetCompatibility = version
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    build {
        dependsOn(shadowJar)
    }
}