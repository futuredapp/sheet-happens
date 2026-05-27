package app.futured.sheethappens.localizer.api

import com.google.auth.oauth2.GoogleCredentials
import java.io.File
import java.net.HttpURLConnection

internal interface AccessTokenProvider {
    fun buildUrl(baseUrl: String): String
    fun applyHeaders(connection: HttpURLConnection)
}

internal class ApiKeyTokenProvider(private val key: String) : AccessTokenProvider {
    override fun buildUrl(baseUrl: String) = "$baseUrl?key=$key"
    override fun applyHeaders(connection: HttpURLConnection) = Unit
}

internal class ServiceAccountTokenProvider(private val keyFile: File) : AccessTokenProvider {
    override fun buildUrl(baseUrl: String) = baseUrl
    override fun applyHeaders(connection: HttpURLConnection) {
        val credentials = GoogleCredentials
            .fromStream(keyFile.inputStream())
            .createScoped("https://www.googleapis.com/auth/spreadsheets.readonly")
        credentials.refreshIfExpired()
        connection.setRequestProperty("Authorization", "Bearer ${credentials.accessToken.tokenValue}")
    }
}
