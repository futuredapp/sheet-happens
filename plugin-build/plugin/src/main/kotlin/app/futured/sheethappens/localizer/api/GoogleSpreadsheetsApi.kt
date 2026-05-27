package app.futured.sheethappens.localizer.api

import app.futured.sheethappens.localizer.model.SpreadsheetResponse
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

internal class GoogleSpreadsheetsApi {

    companion object {
        private const val BaseUrl = "https://sheets.googleapis.com/v4/spreadsheets"
    }

    fun download(spreadsheetId: String, sheetName: String, provider: AccessTokenProvider): SpreadsheetResponse {
        val url = URL(provider.buildUrl("$BaseUrl/$spreadsheetId/values/$sheetName"))
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            provider.applyHeaders(this)
        }

        val response = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }

        return Json.decodeFromString(response)
    }
}
