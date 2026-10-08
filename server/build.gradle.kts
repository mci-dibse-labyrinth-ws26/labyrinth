// Spielserver.
plugins {
    id("labyrinth.java-conventions")
    application
}

dependencies {
    implementation(project(":protocol"))
    implementation(project(":core"))
}

application {
    mainClass = "edu.mci.labyrinth.server.ServerApp"
}
