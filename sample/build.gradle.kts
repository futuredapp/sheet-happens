import java.util.*

val localProperties = Properties().apply { load(rootProject.file("local.properties").inputStream()) }

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sheetHappens)
}

// Preventivka
//sheetHappens {
//    spreadsheetId = "1TGTHG0a2HqfGh7VD5_8wh0F7Cljt0HFwaPQTDmAJ8wk"
//    sheetName = "Sheet1"
//    apiKey = localProperties.getProperty("GOOGLE_API_KEY")
//
//    sheetLayout {
//        sectionColumnName = "section"
//        keyColumnName = "key"
//        languageColumn("EN" to "base")
//        languageColumn("CZ" to "cs")
//    }
//
//    resourcesLayout {
//        resourcesDir = layout.projectDirectory.dir("src/commonMain/resources/MR/")
//        splitResources = true
//    }
//}

// Muzeum
//sheetHappens {
//    spreadsheetId = "1eq7TTBbJ1PyMfiXYIXKm77o4Zvpus_oErgOF3bH0-A4"
//    sheetName = "Translations_draft"
//    apiKey = localProperties.getProperty("GOOGLE_API_KEY")
//
//    sheetLayout {
//        keyColumnName = "key_android"
//        sectionColumnName = "Section"
//        languageColumn("EN" to "values")
//        languageColumn("CZ" to "values-cs")
//        languageColumn("CZ" to "values-sk")
//        languageColumn("UK" to "values-uk")
//    }
//
//    resourcesLayout {
//        resourcesDir = layout.projectDirectory.dir("src/main/res")
//    }
//}

sheetHappens {
    spreadsheetId.set("Spreadsheet ID, eg. bKGtVRjP-m_HNsiZJNE5qWH3FweSNlRQv4tsM1WkF65J7ZgqB_WWqN")
    sheetName.set("Sheet name, eg. Sheet1")
    apiKey.set("API Key ****")

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