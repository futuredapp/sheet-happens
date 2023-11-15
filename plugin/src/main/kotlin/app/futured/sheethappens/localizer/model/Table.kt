package app.futured.sheethappens.localizer.model

internal typealias TableRow = List<String>

internal sealed class TableColumn {
    abstract val index: Int

    data class Section(override val index: Int) : TableColumn()
    data class Key(override val index: Int) : TableColumn()
    data class Translation(override val index: Int, val locale: Locale) : TableColumn()
}

internal operator fun TableRow.get(column: TableColumn?): String? = column?.let { getOrNull(it.index) }

internal fun String.isPluralKey() = contains("##")
internal fun String.pluralKeyModifier() = substringAfter("{").replace("}", "").trim()
internal fun String.pluralKeyValue() = substringBefore("##")