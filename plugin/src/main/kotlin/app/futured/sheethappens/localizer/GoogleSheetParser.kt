package app.futured.sheethappens.localizer

import app.futured.sheethappens.localizer.model.*

// region TODO sort out

internal data class SheetLayout(
    val sectionColumn: String = "section",
    val keyColumn: String = "key",
    val languageMapping: Map<String, String?> = mapOf(
        "EN" to null,
        "CZ" to "cs"
    )
)

// endregion

internal object GoogleSheetParser {

    fun parse(response: SpreadsheetResponse, sheetLayout: SheetLayout): List<SheetEntry> {
        val rows = response.rows
        check(rows.isNotEmpty()) {
            error("Provided spreadsheet is empty")
        }

        // Infer columns based on first sheet row
        val columns = parseColumns(rows.first(), sheetLayout)
        println(columns)
        check(columns.any { it is TableColumn.Key }) {
            "Haven't found any column named \"${sheetLayout.keyColumn}\" in provided Google Sheet"
        }
        check(columns.any { it is TableColumn.Translation }) {
            "Haven't found any columns with translations. Must be at least one of ${sheetLayout.languageMapping.keys}"
        }

        return parseEntries(rows.drop(1), columns)
    }

    private fun parseColumns(row: TableRow, layout: SheetLayout): List<TableColumn> =
        row.mapIndexed { idx, value ->
            when {
                value == layout.sectionColumn -> TableColumn.Section(index = idx)
                value == layout.keyColumn -> TableColumn.Key(index = idx)
                layout.languageMapping.keys.contains(value) -> TableColumn.Translation(
                    index = idx,
                    locale = Locale(columnName = value, resourceQualifier = layout.languageMapping[value])
                )

                else -> null
            }
        }.filterNotNull()

    private fun parseEntries(rows: List<TableRow>, columns: List<TableColumn>): List<SheetEntry> {
        val sectionColumn = columns.find { it is TableColumn.Section } as? TableColumn.Section
        val keyColumn = columns.first { it is TableColumn.Key } as TableColumn.Key
        val translationColumns = columns.filterIsInstance<TableColumn.Translation>()

        return rows
            .mapNotNull { row ->
                val section = row[sectionColumn]
                if (!section.isNullOrBlank()) {
                    return@mapNotNull SheetEntry.Section(title = section)
                }

                val key = row[keyColumn] ?: return@mapNotNull null

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