plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.gradle.pluginPublish)
}

group = "app.futured.sheethappens"
version = "0.5.5"

gradlePlugin {
    website = "https://github.com/futuredapp/sheet-happens"
    vcsUrl = "https://github.com/futuredapp/sheet-happens"

    plugins {
        create("sheetHappensPlugin") {
            id = "app.futured.sheethappens"
            displayName = "Sheet Happens"
            description = "Gradle plugin for generating Android / KMP string translations from Google Spreadsheets"
            implementationClass = "app.futured.sheethappens.plugin.SheetHappensPlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    implementation(libs.kotlinx.serialization.json)
}

tasks.test {
    useJUnitPlatform()
}