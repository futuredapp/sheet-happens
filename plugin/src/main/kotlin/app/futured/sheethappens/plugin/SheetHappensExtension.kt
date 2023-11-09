package app.futured.sheethappens.plugin

import org.gradle.api.provider.Property

/**
 * This [SheetHappensExtension] is used for configuring localization plugin in a Gradle project.
 */
interface SheetHappensExtension {

    /**
     * A Google spreadsheet ID.
     */
    val spreadsheetId: Property<String>

    /**
     * The name of sheet that contains localizations.
     */
    val sheetName: Property<String>

    /**
     * API key used for access to Google spreadsheet API.
     */
    val apiKey: Property<String>
}