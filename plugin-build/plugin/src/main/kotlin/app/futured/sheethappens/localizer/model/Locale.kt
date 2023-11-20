package app.futured.sheethappens.localizer.model

/**
 * The [Locale] maps specific column in Google Sheet identified by [columnName] to resource directory
 * in mobile application.
 */
internal data class Locale(val columnName: String, val subdirectory: String)
