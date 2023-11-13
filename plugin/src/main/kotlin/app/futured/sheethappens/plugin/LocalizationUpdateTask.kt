package app.futured.sheethappens.plugin

import app.futured.sheethappens.localizer.GoogleSheetParser
import app.futured.sheethappens.localizer.ResourcesSerializer
import app.futured.sheethappens.localizer.SheetLayout
import app.futured.sheethappens.localizer.api.GoogleSpreadsheetsApi
import app.futured.sheethappens.localizer.model.Locale
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.ByteArrayOutputStream

abstract class LocalizationUpdateTask : DefaultTask() {

    @get:Input
    @get:Option(option = "spreadsheetId", description = "Spreadsheet ID")
    abstract val spreadsheetId: Property<String>

    @get:Input
    @get:Option(option = "sheetName", description = "The name of sheet in provided spreadsheet")
    abstract val sheetName: Property<String>

    @get:Input
    @get:Option(option = "apiKey", description = "Google Spreadsheets API key for accessing provided sheet")
    abstract val apiKey: Property<String>

    init {
        group = "localization"
    }

    @TaskAction
    fun execute() {
        val response = GoogleSpreadsheetsApi().download(spreadsheetId.get(), sheetName.get(), apiKey.get())
        val sheetEntries = GoogleSheetParser.parse(response, SheetLayout())

        val outputStream = ByteArrayOutputStream()
        ResourcesSerializer.serialize(sheetEntries, Locale("CZ", "cs"), outputStream)

        println(outputStream.toString(Charsets.UTF_8))
    }
}