// Gemeinsame Einstellungen für alle Java-Module im Projekt.
// Ein Modul übernimmt sie mit: plugins { id("labyrinth.java-conventions") }

plugins {
    java
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

testing {
    suites.named<JvmTestSuite>("test") {
        useJUnitJupiter(libs.findVersion("junit").get().requiredVersion)
    }
}
