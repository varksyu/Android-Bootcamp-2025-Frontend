package ru.sicampus.bootcamp2025.data.center

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2025.data.Network.client
import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource.token

class CenterNetworkDataSource {
    suspend fun getCenters(lat : Double? = null, lng : Double? = null): Result<List<CenterDto>> = withContext(Dispatchers.IO) {
        runCatching {
            Log.d("Zapros", "начал отправлять запросы")
            val result = client.get("https://localhost:8081/api/center/closest?${lat}&${lng}") {
                header(HttpHeaders.Authorization, token)
            }


            Log.d("serverCode", "${result.status}")
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}")
            }
            Log.d("result", result.bodyAsText())
            result.body()
        }
    }
}