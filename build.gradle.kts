plugins {
    id ("fabric-loom") version "1.5-SNAPSHOT"
    id ("maven-publish")
    kotlin("jvm") version "2.3.20"
}

version = "1.0.0"
group = "net.vnnhattruongneee"

repositories {
    maven("https://maven.shedaniel.me/") // Cloth Config
    maven("https://maven.terraformersmc.com/releases/") // ModMenu
    
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.5")
    mappings("net.fabricmc:yarn:1.21.5+build.1:v2")
    modImplementation("net.fabricmc:fabric-loader:0.18.0")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.128.2+1.21.5")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.10+kotlin.2.3.20")

    // ModMenu & Cloth Config
    modImplementation("com.terraformersmc:modmenu:11.0.0")
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:15.0.128")
}

kotlin {
    jvmToolchain(21)
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

base {
	archivesName = "PacketSpy-Fabric-1.21.5+"
}
