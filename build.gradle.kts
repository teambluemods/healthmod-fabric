import net.fabricmc.loom.task.RunClientTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "0.5-SNAPSHOT"
    java
    kotlin("jvm") version "1.4.0"
    idea
    id("net.minecrell.licenser") version "0.4.1"
    id("maven-publish")
}

fun property(name: String) = project.findProperty(name)!!

base.archivesBaseName = property("archives_base_name").toString()
version = property("mod_version")
group = property("maven_group")

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }

    sourceSets.create("datagen") {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
}

repositories {
    // for noauth
    maven("https://dl.bintray.com/user11681/maven")
    // for cloth api
    maven("https://dl.bintray.com/shedaniel/cloth/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    // fapi
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    // kotlin adapter
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    // yeet mojank console spam
    modRuntime("user11681:noauth:+")

    // yes
    modImplementation("io.github.prospector:modmenu:${property("modmenu_version")}")

    // datagen
    modApi("me.shedaniel.cloth.api:cloth-datagen-api-v1:${property("cloth_api_version")}")
    include("me.shedaniel.cloth.api:cloth-datagen-api-v1:${property("cloth_api_version")}")

    // note: older mods on loom 0.2.1 might need transitiveness disabled
    "datagenCompile"(sourceSets.main.get().output)
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<JavaCompile>().configureEach {
        // this fixes some edge cases with special characters not displaying correctly
        // if Javadoc is generated, this must be specified in that task too.
        options.encoding = "UTF-8"

        // The Minecraft launcher currently installs Java 8 for users, so your mod probably wants to target Java 8 too
        // JDK 9 introduced a new way of specifying this that will make sure no newer classes or methods are used.
        // We'll use that if it's available, but otherwise we'll use the older option.
        val targetVersion = "8"

        if (JavaVersion.current().isJava9Compatible) {
            options.compilerArgs.addAll(listOf("--release", targetVersion))
        }
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    jar {
        from("COPYING.md") {
            rename { "${it}_${project.base.archivesBaseName}" }
        }
    }

    create(name = "generateData", type = RunClientTask::class) {
        classpath = configurations.runtimeClasspath.get()
        classpath(sourceSets["main"].output)
        classpath(sourceSets["datagen"].output)
    }
}

java {
    // generate remapped sources jar
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// this sets the header for licenser plugin to add
license {
    header = rootProject.file("LICENSE_HEADER.txt")

    ext {
        set("name", "Blue Minecraft Team")
        set("years", "2020")
        set("projectName", "HealthMod")
    }

    include("**/*.java")
    include("**/*.kt")
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            // add all the jars that should be included when publishing to maven
            artifact(tasks.remapJar.get()) {
                builtBy(tasks.remapJar.get())
            }
            artifact(tasks["sourcesJar"]) {
                builtBy(tasks.remapSourcesJar.get())
            }
        }
    }

    // repositories to publish to
    repositories {
    }
}