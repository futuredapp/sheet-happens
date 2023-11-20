package app.futured.sheethappens.localizer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class which stores API response from Spreadsheets API.
 */
@Serializable
internal data class SpreadsheetResponse(

    @SerialName("range")
    val range: String,

    @SerialName("majorDimension")
    val majorDimension: String,

    @SerialName("values")
    val rows: List<List<String>>,
)
