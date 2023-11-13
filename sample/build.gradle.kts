import java.util.Properties

val localProperties = Properties().apply { load(rootProject.file("local.properties").inputStream()) }

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("app.futured.sheethappens") version "0.0.5"
}

// Preventivka
sheetHappens {
    spreadsheetId = "1TGTHG0a2HqfGh7VD5_8wh0F7Cljt0HFwaPQTDmAJ8wk"
    sheetName = "Sheet1"
    apiKey = localProperties.getProperty("GOOGLE_API_KEY")
}

// Muzeum
//sheetHappens {
//    spreadsheetId = "1eq7TTBbJ1PyMfiXYIXKm77o4Zvpus_oErgOF3bH0-A4" // NM
//    sheetName = "Translations_draft"
//    apiKey = localProperties.getProperty("GOOGLE_API_KEY")
//}