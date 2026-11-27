// Gemeinsame Einstellungen für alle Java-Module im Projekt.
// Ein Modul übernimmt sie mit: plugins{ id("labyrinth.java-conventions") }

plugins {
    java
    jacoco
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

// Testabdeckung mit JaCoCo (NF-10). Nach jedem Testlauf entsteht ein Bericht.
tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
    }
}

// Mindestwerte der Testabdeckung (Testplan 5.1, NF-10): Unterschreitet ein
// Modul einen Wert aus seinem jacocoTestCoverageVerification, scheitert check
// und damit build (AIP6-67).
tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
