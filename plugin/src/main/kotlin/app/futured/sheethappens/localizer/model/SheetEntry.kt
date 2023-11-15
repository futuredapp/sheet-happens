package app.futured.sheethappens.localizer.model

/**
 * This is the domain model which represents one row in Google Sheet.
 * Each row, aka. "entry" can contain one of:
 * - section title
 * - plaintext translation
 * - plural translation
 *
 * For each translation, there will be one key to represent that string with a number of values, one for each [Locale]
 * stored in specific column.
 */
internal sealed interface SheetEntry {

    data class Section(val comment: String) : SheetEntry

    sealed interface Translation {
        val locales: Set<Locale>
    }

    data class PlainResource(val items: Map<Locale, Item>) : SheetEntry, Translation {

        override val locales: Set<Locale> = items.keys

        data class Item(
            val key: String,
            val value: String,
        )
    }

    data class PluralResource(val items: Map<Locale, Item>) : SheetEntry, Translation {

        override val locales: Set<Locale> = items.keys

        data class Item(
            val key: String,
            val quantityModifier: String,
            val value: String,
        )
    }
}
