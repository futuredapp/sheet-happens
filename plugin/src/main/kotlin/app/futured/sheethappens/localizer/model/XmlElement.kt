package app.futured.sheethappens.localizer.model

/**
 * Single XML entry/element representable by either a plain string resource of plural string resource.
 * Each element has specific XML representation.
 * Each element can have series of XML comments prepended before its XML tag.
 */
internal sealed interface XmlElement {
    val comments: List<String>

    /**
     * Plain string resource defined as
     *
     * ```xml
     * <!-- [comment] -->
     * <string name="[key]">[value]</string>
     * ```
     */
    data class PlainResource(
        val key: String,
        val value: String,
        override val comments: List<String> = emptyList(),
    ) : XmlElement

    /**
     * Plural string resource defined as
     *
     * ```xml
     * <!-- [comment] -->
     * <plurals name="[key]">
     *     <item quantity="[item.quantityModifier]">[item.value]</item>
     * </plurals>
     * ```
     */
    data class PluralResource(
        val key: String,
        val items: List<Item>,
        override val comments: List<String> = emptyList(),
    ) : XmlElement {
        data class Item(val value: String, val quantityModifier: String)
    }
}
