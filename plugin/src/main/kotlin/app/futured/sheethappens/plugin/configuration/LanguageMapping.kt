package app.futured.sheethappens.plugin.configuration

import java.io.Serializable

/**
 * Represents language mapping of Sheet column to directory in which resources will be generated.
 *
 * @param columnName Name of Sheet column
 * @param subdirectory Resource subdirectory name, eg. "values", or "values-en".
 */
data class LanguageMapping(
    val columnName: String,
    val subdirectory: String
) : Serializable