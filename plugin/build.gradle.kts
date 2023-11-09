plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.pluginPublish)
}

group = "app.futured.sheethappens"
version = "0.0.1"

gradlePlugin {
    website = "https://github.com/futuredapp/sheet-happens"
    vcsUrl = "git@github.com:futuredapp/sheet-happens.git"

    plugins {
        create("sheetHappensPlugin") {
            id = "app.futured.sheethappens"
            displayName = "Sheet Happens"
            description = "Gradle plugin for generating Android / KMP string translations generates from Google Sheets"
            implementationClass = "app.futured.sheethappens.SheetHappensPlugin"
        }
    }
}