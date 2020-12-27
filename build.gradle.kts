import groovy.lang.MissingPropertyException
import net.fabricmc.loom.task.RunClientTask
import org.gradle.api.tasks.compile.JavaCompile as CompileJava
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile as CompileKotlin

plugins {
    java
    kotlin("jvm") version "1.4.21"
    id("fabric-loom") version "0.5-SNAPSHOT"
    id("maven-publish")
    id("net.minecrell.licenser") version "0.4.1"
}

fun propertyOrEnv(name: String, envName: String = name) = project.findProperty(name)
    ?: System.getenv(envName)
    ?: MissingPropertyException("Property $name/environment variable $envName was not found")

// getters for if a property exists
fun hasPropertyOrEnv(
    name: String,
    envName: String = name
) = project.hasProperty(name) || System.getenv().containsKey(envName)

base.archivesBaseName = project.property("archives_base_name").toString()
version = project.property("mod_version")!!
group = project.property("maven_group")!!

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
    maven("https://dl.bintray.com/user11681/maven") { name = "user11681" }
    // for cloth api
    maven("https://dl.bintray.com/shedaniel/cloth/") { name = "shedaniel" }

    maven("https://jitpack.io") { name = "Jitpack" }

    maven("https://maven.abusedmaster.xyz") { name = "OnyxStudios" }

    jcenter { name = "JCenter" }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    // fapi
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    // kotlin adapter
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")

    // yeet mojank console spam
    modRuntime("user11681:noauth:+")

    modApi("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-base:${project.property("cca_version")}")
    include("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-base:${project.property("cca_version")}")
    // for entity components
    modImplementation("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-entity:${project.property("cca_version")}")
    include("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-entity:${project.property("cca_version")}")

    // yes
    modImplementation("io.github.prospector:modmenu:${project.property("mod_menu_version")}")

    // config
    modApi("me.shedaniel.cloth:config-2:${project.property("cloth_config_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    include("me.shedaniel.cloth:config-2:${project.property("cloth_config_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi("me.sargunvohra.mcmods:autoconfig1u:${project.property("auto_config_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    include("me.sargunvohra.mcmods:autoconfig1u:${project.property("auto_config_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    // datagen
    modApi("me.shedaniel.cloth.api:cloth-datagen-api-v1:${project.property("cloth_api_version")}")
    include("me.shedaniel.cloth.api:cloth-datagen-api-v1:${project.property("cloth_api_version")}")

    // note: older mods on loom 0.2.1 might need transitiveness disabled
    "datagenCompile"(sourceSets.main.get().output)
}

tasks {
    withType<CompileKotlin>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.languageVersion = "1.4"
    }

    withType<CompileJava>().configureEach {
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

    withType<ProcessResources>().configureEach {
        /*
         * DO NOT USE INTELLIJ FOR BUILDING!!!
         * It may be faster, but it will break the mod, as the IntelliJ compiler does not support this task.
         */
        val toReplace = listOf(
            "mod_version",
            "minecraft_version",
            "loader_version",
            "fabric_version",
            "fabric_kotlin_version",
            "mod_menu_version"
        ).map { it to project.property(it).toString() }.toMap()

        inputs.properties(toReplace)

        filesMatching("fabric.mod.json") {
            expand(toReplace)
        }
    }

    jar {
        from("COPYING.md") {
            rename { "${it}_${project.base.archivesBaseName}" }
        }

        from("COPYING.LESSER.md") {
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
        create<MavenPublication>("maven") {
            // add all the jars that should be included when publishing to maven

            artifact(tasks["sourcesJar"]) {
                builtBy(tasks["remapSourcesJar"])
            }

            afterEvaluate {
                artifact(tasks["remapJar"]) {
                    artifactId = "healthmod-fabric"
                }
            }
        }
    }

    // repositories to publish to
    repositories {
        if (hasPropertyOrEnv(name = "modMavenUrl", envName = "MOD_MAVEN_URL")
            && hasPropertyOrEnv(name = "modMavenUsername", envName = "MOD_MAVEN_USERNAME")
            && hasPropertyOrEnv(name = "modMavenPassword", envName = "MOD_MAVEN_PASSWORD")
        ) {
            maven(propertyOrEnv(name = "modMavenUrl", envName = "MOD_MAVEN_URL")) {
                credentials {
                    username = propertyOrEnv(name = "modMavenUsername", envName = "MOD_MAVEN_USERNAME").toString()
                    password = propertyOrEnv(name = "modMavenPassword", envName = "MOD_MAVEN_PASSWORD").toString()
                }
            }
        }
    }
}