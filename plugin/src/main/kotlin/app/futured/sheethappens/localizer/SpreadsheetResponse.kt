package app.futured.sheethappens.localizer

import kotlinx.serialization.Serializable

internal typealias SpreadsheetRow = List<String>

@Serializable
internal data class SpreadsheetResponse(
    val range: String,
    val majorDimension: String,
    val values: List<SpreadsheetRow>
)