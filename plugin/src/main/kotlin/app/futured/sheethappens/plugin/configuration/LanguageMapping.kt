package app.futured.sheethappens.plugin.configuration

/**
 * Represents language mapping of Sheet column to generated resource qualifier.
 *
 * @param columnName Name of Sheet column
 * @param resourceQualifier Android resource qualifier, or `null` if no resource qualifier should be used for this language.
 */
data class LanguageMapping(val columnName: String, val resourceQualifier: String?)