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
