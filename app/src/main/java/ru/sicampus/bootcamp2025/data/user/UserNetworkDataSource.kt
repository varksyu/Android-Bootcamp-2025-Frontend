package ru.sicampus.bootcamp2025.data.user

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2025.data.Network.client
import ru.sicampus.bootcamp2025.data.center.CenterDto

class UserNetworkDataSource {
    suspend fun getUser(id : Int): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            Log.d("Zapros", "начал отправлять запросы")
            val result = client.get("http://10.0.2.2:8081/api/user/${id}") //10.0.2.2

            Log.d("serverCode", "${result.status}")
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}")
            }
            Log.d("result", "${result.bodyAsText()}")
            result.body()
        }
    }
}