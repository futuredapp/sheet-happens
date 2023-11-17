package app.futured.sheethappens.localizer

import app.futured.sheethappens.localizer.model.Locale
import app.futured.sheethappens.localizer.model.SheetEntry
import app.futured.sheethappens.localizer.model.XmlElement

/**
 * Object responsible for accumulating list of [SheetEntry] objects to individual [XmlElement] objects.
 */
internal object SheetEntryAccumulator {

    /**
     * Takes provided [sheetEntries] and converts them to list of [XmlElement] elements,
     * selecting only translations that match provided [locale].
     *
     * Each [XmlElement] will be assigned comments which appear right before this element.
     */
    fun accumulateToXmlElements(sheetEntries: List<SheetEntry>, locale: Locale): List<XmlElement> {
        val commentsAccumulator = mutableListOf<String>()
        var pluralAccumulator: XmlElement.PluralResource? = null

        val xmlEntries = mutableListOf<XmlElement>()

        for (index in sheetEntries.indices) {
            val sheetEntry = sheetEntries[index]
            val nextSheetEntry = sheetEntries.getOrNull(index + 1)

            when (sheetEntry) {
                is SheetEntry.Section -> commentsAccumulator += sheetEntry.comment
                is SheetEntry.PlainResource -> {
                    val translation = sheetEntry.items[locale] ?: continue
                    xmlEntries += XmlElement.PlainResource(
                        key = translation.key,
                        value = translation.value,
                        comments = commentsAccumulator.toList(),
                    )
                    commentsAccumulator.clear()
                }

                is SheetEntry.PluralResource -> {
                    val translation = sheetEntry.items[locale] ?: continue
                    if (pluralAccumulator == null) {
                        // If this is the first plural encountered,
                        // initialize accumulator with the key and add first translation to items
                        pluralAccumulator = XmlElement.PluralResource(
                            key = translation.key,
                            items = listOf(
                                XmlElement.PluralResource.Item(
                                    translation.value,
                                    translation.quantityModifier,
                                ),
                            ),
                            comments = commentsAccumulator.toList(),
                        )
                        commentsAccumulator.clear()
                    } else if (translation.key == pluralAccumulator.key) {
                        // Continue accumulating the same plural
                        pluralAccumulator = pluralAccumulator.copy(
                            items = pluralAccumulator.items + XmlElement.PluralResource.Item(
                                value = translation.value,
                                quantityModifier = translation.quantityModifier,
                            ),
                        )
                    }
                }
            }

            if (pluralAccumulator != null) {
                if (nextSheetEntry !is SheetEntry.PluralResource) {
                    // Finish accumulation because next element is not plural, or is end of list
                    xmlEntries += pluralAccumulator.copy()
                    pluralAccumulator = null
                    continue
                }

                if (nextSheetEntry.items[locale]?.key != pluralAccumulator.key) {
                    // Finish accumulation because next element is plural but with different key
                    xmlEntries += pluralAccumulator.copy()
                    pluralAccumulator = null
                    continue
                }
            }
        }

        return xmlEntries.toList()
    }
}
