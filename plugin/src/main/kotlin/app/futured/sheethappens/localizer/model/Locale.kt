package app.futured.sheethappens.localizer.model

/**
 * The [Locale] maps specific column in Google Sheet identified by [columnName] to resource qualifier
 * in mobile application.
 *
 * The [resourceQualifier] is optional, and `null` indicates that no resource qualifier should be used
 * for translation selected by this [Locale].
 */
internal data class Locale(val columnName: String, val resourceQualifier: String?)