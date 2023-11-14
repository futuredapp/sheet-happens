package app.futured.sheethappens.plugin

import app.futured.sheethappens.localizer.GoogleSheetParser
import app.futured.sheethappens.localizer.ResourcesSerializer
import app.futured.sheethappens.localizer.api.GoogleSpreadsheetsApi
import app.futured.sheethappens.localizer.model.SheetEntry
import app.futured.sheethappens.plugin.configuration.LanguageMapping
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.nio.file.Files

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

    @get:Input
    @get:Option(option = "sectionColumn", description = "Name of Google Sheet column that contains section headers")
    @get:Optional
    abstract val sectionColumnName: Property<String>

    @get:Input
    @get:Option(option = "keyColumn", description = "Name of Google Sheet column that contains translation keys")
    abstract val keyColumnName: Property<String>

    @get:Input
    @get:Option(
        option = "languageMapping",
        description = "Mapping of column names to resource qualifiers for each language in Google Sheet"
    )
    abstract val languageMapping: ListProperty<LanguageMapping>

    @get:InputDirectory
    @get:Option(
        option = "resourcesFolder",
        description = "Folder where to generate resources"
    )
    abstract val resourcesFolder: DirectoryProperty

    init {
        group = "localization"
    }

    @TaskAction
    fun execute() {
        val response = GoogleSpreadsheetsApi().download(
            spreadsheetId = spreadsheetId.get(),
            sheetName = sheetName.get(),
            apiKey = apiKey.get()
        )
        val sheetEntries = GoogleSheetParser.parse(
            response = response,
            sectionColumn = sectionColumnName.orNull,
            keyColumn = keyColumnName.get(),
            languageMapping = languageMapping.get()
        )

        val detectedLocales = sheetEntries
            .filterIsInstance<SheetEntry.Translation>()
            .flatMap { it.resources.map { resource -> resource.locale } }
            .distinct()

        for (locale in detectedLocales) {
            val targetDir = when(val resourceQualifier = locale.resourceQualifier) {
                null -> resourcesFolder.dir("values")
                else -> resourcesFolder.dir("values-$resourceQualifier")
            }

            Files.createDirectories(targetDir.get().asFile.toPath())
            val stringsFile = targetDir.map { directory -> directory.file("strings.xml") }.get().asFile
            if (stringsFile.exists()) {
                stringsFile.delete()
            }
            stringsFile.createNewFile()

            ResourcesSerializer.serialize(sheetEntries, locale, stringsFile.outputStream())
        }
    }
}