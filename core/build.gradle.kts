// Spielmodell und Spielregeln. Wird von Server und Client (KI) gemeinsam genutzt.
plugins {
    id("labyrinth.java-conventions")
}

// Mindestwerte für die Testabdeckung (Testplan 5.1, NF-10).
// Teil von check: Ein Unterschreiten lässt den Build scheitern (AIP6-67).
tasks.jacocoTestCoverageVerification {
    violationRules {
        // core gesamt: mindestens 80 % Zeilenabdeckung
        rule {
            limit {
                counter = "LINE"
                minimum = "0.80".toBigDecimal()
            }
        }
        // Prüfkatalog im Spielkern (V-05 bis V-12, V-15): 100 % Zweigabdeckung.
        // Greift erst, sobald RuleValidator existiert.
        rule {
            element = "CLASS"
            includes = listOf("edu.mci.labyrinth.core.RuleValidator*")
            limit {
                counter = "BRANCH"
                minimum = "1.0".toBigDecimal()
            }
        }
    }
}
