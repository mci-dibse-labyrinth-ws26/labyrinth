// Verzeichnisserver: Liste der laufenden Spielserver.
plugins {
    id("labyrinth.java-conventions")
    application
}

dependencies {
    implementation(project(":protocol"))
}

application {
    mainClass = "edu.mci.labyrinth.directory.DirectoryServerApp"
}
