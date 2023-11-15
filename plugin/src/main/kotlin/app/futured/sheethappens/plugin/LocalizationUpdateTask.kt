package app.futured.sheethappens.plugin

import app.futured.sheethappens.localizer.GoogleSheetParser
import app.futured.sheethappens.localizer.ResourcesSerializer
import app.futured.sheethappens.localizer.SheetEntryAccumulator
import app.futured.sheethappens.localizer.api.GoogleSpreadsheetsApi
import app.futured.sheethappens.localizer.model.SheetEntry
import app.futured.sheethappens.localizer.model.XmlElement
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
import java.io.File
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
        description = "Mapping of column names to subdirectories for each language in Google Sheet"
    )
    abstract val languageMapping: ListProperty<LanguageMapping>

    @get:InputDirectory
    @get:Option(
        option = "resourcesDir",
        description = "Folder where to generate resources"
    )
    abstract val resourcesDir: DirectoryProperty

    @get:Input
    @get:Option(
        option = "stringsFileName",
        description = "Name of file where string resources will be generated"
    )
    @get:Optional
    abstract val stringsFileName: Property<String>

    @get:Input
    @get:Option(
        option = "pluralsFileName",
        description = "Name of file where plural resources will be generated"
    )
    @get:Optional
    abstract val pluralsFileName: Property<String>

    init {
        group = "localization"
    }

    @TaskAction
    fun execute() {
        val sectionColumnName = sectionColumnName.getOrElse("section")
        val stringsFileName = stringsFileName.getOrElse("strings")
            .substringBefore(".xml").plus(".xml")
        val pluralsFileName = pluralsFileName.getOrElse("plurals")
            .substringBefore(".xml").plus(".xml")

        val apiResponse = GoogleSpreadsheetsApi().download(
            spreadsheetId = spreadsheetId.get(),
            sheetName = sheetName.get(),
            apiKey = apiKey.get()
        )
        val sheetEntries = GoogleSheetParser.parse(
            response = apiResponse,
            sectionColumn = sectionColumnName,
            keyColumn = keyColumnName.get(),
            languageMapping = languageMapping.get()
        )
        val detectedLocales = sheetEntries
            .filterIsInstance<SheetEntry.Translation>()
            .flatMap { it.locales }
            .distinct()

        detectedLocales.forEach { locale ->
            val localeSubdirectory = resourcesDir.dir(locale.subdirectory)

            Files.createDirectories(localeSubdirectory.get().asFile.toPath())
            val stringsFile = localeSubdirectory.map { directory -> directory.file(stringsFileName) }.get().asFile
            val pluralsFile = localeSubdirectory.map { dir -> dir.file(pluralsFileName) }.get().asFile

            val xmlElements = SheetEntryAccumulator.accumulateToXmlElements(sheetEntries, locale)

            if (xmlElements.any { it is XmlElement.PlainResource }) {
                stringsFile.recreate()
                ResourcesSerializer.serialize(
                    xmlElements = xmlElements.filterIsInstance<XmlElement.PlainResource>(),
                    outputStream = stringsFile.outputStream()
                )
            }

            if (xmlElements.any { it is XmlElement.PluralResource }) {
                pluralsFile.recreate()
                ResourcesSerializer.serialize(
                    xmlElements = xmlElements.filterIsInstance<XmlElement.PluralResource>(),
                    outputStream = pluralsFile.outputStream()
                )
            }
        }
    }
}

private fun File.recreate() {
    if (exists()) {
        delete()
    }
    createNewFile()
}