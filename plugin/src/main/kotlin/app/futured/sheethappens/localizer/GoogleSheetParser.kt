package app.futured.sheethappens.localizer

import app.futured.sheethappens.localizer.model.*
import app.futured.sheethappens.plugin.configuration.LanguageMapping

internal object GoogleSheetParser {

    fun parse(
        response: SpreadsheetResponse,
        sectionColumn: String?,
        keyColumn: String,
        languageMapping: List<LanguageMapping>
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
        languageMapping: List<LanguageMapping>
    ): List<TableColumn> =
        row
            .mapIndexed { idx, value ->
                when {
                    value == sectionColumn -> TableColumn.Section(index = idx)
                    value == keyColumn -> TableColumn.Key(index = idx)
                    languageMapping.any { it.columnName == value } -> {
                        val mapping = languageMapping.first { it.columnName == value }
                        TableColumn.Translation(
                            index = idx,
                            locale = Locale(mapping.columnName, mapping.resourceQualifier)
                        )
                    }

                    else -> null
                }
            }
            .filterNotNull()

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

                return@mapNotNull SheetEntry.Translation(
                    resources = translationColumns.mapNotNull resource@{ translationColumn ->
                        val translation = row[translationColumn]
                        if (translation.isNullOrBlank()) {
                            return@resource null
                        }
                        if (key.isPluralKey()) {
                            Resource.Plural(
                                key = key.pluralKeyValue(),
                                value = translation,
                                locale = translationColumn.locale,
                                modifier = key.pluralKeyModifier()
                            )
                        } else {
                            Resource.Plain(
                                key = key,
                                value = translation,
                                locale = translationColumn.locale
                            )
                        }
                    }
                )
            }
    }
}