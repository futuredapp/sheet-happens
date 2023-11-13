package app.futured.sheethappens.localizer.model

internal sealed interface SheetEntry {
    data class Section(val title: String) : SheetEntry
    data class Translation(val resources: List<Resource>) : SheetEntry
}

internal sealed interface Resource {
    val key: String
    val value: String
    val locale: Locale

    data class Plain(
        override val key: String,
        override val value: String,
        override val locale: Locale
    ) : Resource

    data class Plural(
        override val key: String,
        override val value: String,
        override val locale: Locale,
        val modifier: String // "zero", "one", "two", "few", "many", "other
    ) : Resource
}
