package app.futured.sheethappens.localizer

import app.futured.sheethappens.localizer.model.Locale
import app.futured.sheethappens.localizer.model.SheetEntry
import app.futured.sheethappens.localizer.model.SpreadsheetResponse
import app.futured.sheethappens.localizer.model.TableColumn
import app.futured.sheethappens.localizer.model.TableRow
import app.futured.sheethappens.localizer.model.get
import app.futured.sheethappens.localizer.model.isPluralKey
import app.futured.sheethappens.localizer.model.pluralKeyModifier
import app.futured.sheethappens.localizer.model.pluralKeyValue
import app.futured.sheethappens.plugin.configuration.LanguageMapping

internal object GoogleSheetParser {

    fun parse(
        response: SpreadsheetResponse,
        sectionColumn: String?,
        keyColumn: String,
        languageMapping: List<LanguageMapping>,
    ): List<SheetEntry> {
        val rows = response.rows
        check(rows.isNotEmpty()) { error("Provided spreadsheet is empty") }
        check(languageMapping.isNotEmpty()) { error("No language mapping was provided.") }

        // Infer columns based on first row of Sheet
        val parsedColumns = parseColumns(rows.first(), sectionColumn, keyColumn, languageMapping)
        check(parsedColumns.any { it is TableColumn.Key }) {
            "Haven't found any column named \"${keyColumn}\" in provided Google Sheet"
        }
        check(parsedColumns.any { it is TableColumn.Translation }) {
            "Google Sheet does not contain any columns specified in language mapping.\n" +
                "Columns specified: ${languageMapping.map { it.columnName }.joinToString()}"
        }

        return parseEntries(rows.drop(1), parsedColumns)
    }

    private fun parseColumns(
        row: TableRow,
        sectionColumn: String?,
        keyColumn: String,
        languageMapping: List<LanguageMapping>,
    ): List<TableColumn> = buildList {
        // Find section column
        row.indexOf(sectionColumn)
            .takeIf { index -> index >= 0 }
            ?.let { index -> TableColumn.Section(index = index) }
            ?.also { add(it) }

        // Find key column
        row.indexOf(keyColumn)
            .takeIf { index -> index >= 0 }
            ?.let { index -> TableColumn.Key(index = index) }
            ?.also { add(it) }

        // Find all language columns
        languageMapping.forEach { mapping ->
            row.indexOf(mapping.columnName)
                .takeIf { index -> index >= 0 }
                ?.let { index -> TableColumn.Translation(index, mapping.toLocale()) }
                ?.also { add(it) }
        }
    }

    private fun LanguageMapping.toLocale() = Locale(columnName, subdirectory)

    private fun parseEntries(rows: List<TableRow>, columns: List<TableColumn>): List<SheetEntry> {
        val sectionColumn = columns.find { it is TableColumn.Section } as? TableColumn.Section
        val keyColumn = columns.first { it is TableColumn.Key } as TableColumn.Key
        val translationColumns = columns.filterIsInstance<TableColumn.Translation>()

        return rows
            .mapNotNull { row ->
                val section = row[sectionColumn]
                if (!section.isNullOrBlank()) {
                    return@mapNotNull SheetEntry.Section(comment = section)
                }

                val key = row[keyColumn].takeIf { !it.isNullOrBlank() } ?: return@mapNotNull null

                if (key.isPluralKey()) {
                    return@mapNotNull SheetEntry.PluralResource(
                        items = translationColumns
                            .map { column ->
                                val columnValue = row[column] ?: return@map null
                                column.locale to SheetEntry.PluralResource.Item(
                                    key = key.pluralKeyValue(),
                                    quantityModifier = key.pluralKeyModifier(),
                                    value = columnValue,
                                )
                            }
                            .filterNotNull()
                            .toMap(),
                    )
                } else {
                    return@mapNotNull SheetEntry.PlainResource(
                        items = translationColumns
                            .map { column ->
                                val columnValue = row[column] ?: return@map null
                                column.locale to SheetEntry.PlainResource.Item(
                                    key = key,
                                    value = columnValue,
                                )
                            }
                            .filterNotNull()
                            .toMap(),
                    )
                }
            }
    }
}
