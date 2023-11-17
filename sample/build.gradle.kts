import java.util.*

val localProperties = Properties().apply { load(rootProject.file("local.properties").inputStream()) }

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sheetHappens)
}

sheetHappens {
    spreadsheetId.set("1q5WMvNFEJQts4lWkSTHN5je_BG3Wq6iRZPseN-ZHrNM")
    sheetName.set("Sheet1")
    apiKey.set(localProperties.getProperty("GOOGLE_API_KEY"))

    sheetLayout {
        sectionColumnName.set("section") // Optional
        keyColumnName.set("key")

        // Add language column for each translation
        languageColumn("EN" to "values")
        languageColumn("SK" to "values-sk")
    }

    resourcesLayout {
        resourcesDir.set(layout.projectDirectory.dir("src/main/res"))

        splitResources = true // Optional, default `false`
        stringsFileName = "strings.xml" // Optional, default "strings.xml"
        pluralsFileName = "plurals.xml" // Optional, default "plurals.xml"
    }
}