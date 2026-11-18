// Spielserver mit JavaFX-Oberfläche; mit --headless ohne Fenster startbar.
plugins {
    id("labyrinth.java-conventions")
    application
    alias(libs.plugins.javafx)
}

dependencies {
    implementation(project(":protocol"))
    implementation(project(":core"))
}

javafx {
    version = libs.versions.javafx.get()
    modules("javafx.controls")
}

application {
    mainClass = "edu.mci.labyrinth.server.ServerApp"
}

// Startklasse und JavaFX-Fenster werden nicht gemessen (Testplan 5.2).
val coverageExcludes = listOf(
    "edu/mci/labyrinth/server/ServerApp*",
    "edu/mci/labyrinth/server/ServerWindow*",
)

tasks.withType<JacocoReportBase>().configureEach {
    classDirectories.setFrom(
        sourceSets.main.get().output.classesDirs.asFileTree.matching {
            exclude(coverageExcludes)
        }
    )
}

// Mindestwert für die Testabdeckung (Testplan 5.1, NF-10).
// Noch nicht Teil von check; wird zum Start von Phase 3 scharf geschaltet (AIP6-15).
tasks.jacocoTestCoverageVerification {
    violationRules {
        // server gesamt: mindestens 70 % Zeilenabdeckung
        rule {
            limit {
                counter = "LINE"
                minimum = "0.70".toBigDecimal()
            }
        }
    }
}
