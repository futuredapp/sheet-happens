import java.util.*

val localProperties = Properties().apply { load(rootProject.file("local.properties").inputStream()) }

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("app.futured.sheethappens") version "0.0.7"
}

sheetHappens {
    spreadsheetId = "1TGTHG0a2HqfGh7VD5_8wh0F7Cljt0HFwaPQTDmAJ8wk"
    sheetName = "Sheet1"
    apiKey = localProperties.getProperty("GOOGLE_API_KEY")

    sheetLayout {
        sectionColumnName = "section"
        keyColumnName = "key"
        languageMapping("EN" to null)
        languageMapping("CZ" to "cs")
    }
}

// Muzeum
//sheetHappens {
//    spreadsheetId = "1eq7TTBbJ1PyMfiXYIXKm77o4Zvpus_oErgOF3bH0-A4" // NM
//    sheetName = "Translations_draft"
//    apiKey = localProperties.getProperty("GOOGLE_API_KEY")
//
//    sheetLayout {
//        keyColumnName = "key_android"
//        sectionColumnName = "Section"
//        languageMapping("EN" to null)
//        languageMapping("CZ" to "cs")
//    }
//}