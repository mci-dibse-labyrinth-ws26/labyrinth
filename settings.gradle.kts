plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "labyrinth"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "protocol",
    "core",
    "server",
    "directory-server",
    "client-desktop",
)
