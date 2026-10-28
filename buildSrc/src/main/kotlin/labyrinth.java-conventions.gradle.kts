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

// Javadoc-Pflicht für öffentliche Klassen und Methoden (Pflichtenheft NF-09).
// Jede Warnung von javadoc, etwa ein fehlender Kommentar oder ein fehlendes
// @param, lässt den Build scheitern.
tasks.javadoc {
    val javadocOptions = options as StandardJavadocDocletOptions
    javadocOptions.addBooleanOption("Xdoclint:all", true)
    javadocOptions.addBooleanOption("Werror", true)
}

tasks.check {
    dependsOn(tasks.javadoc)
}
