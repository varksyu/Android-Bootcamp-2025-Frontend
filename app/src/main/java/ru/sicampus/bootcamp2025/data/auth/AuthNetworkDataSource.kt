package ru.sicampus.bootcamp2025.data.auth

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2025.data.Network.client
import ru.sicampus.bootcamp2025.data.center.CenterDto
import ru.sicampus.bootcamp2025.data.user.UserDto
import ru.sicampus.bootcamp2025.domain.auth.IsUserExistUseCase

object AuthNetworkDataSource {

    suspend fun IsUserExist(login: String): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.get("http://10.0.2.2:8081/api/person/username/$login") //10.0.2.2
            result.status == HttpStatusCode.OK
        }
    }

    suspend fun login(token: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.get("http://10.0.2.2:8081/api/person/login") //10.0.2.2
            headers {
                append(HttpHeaders.Authorization, token)
            }
            if (result.status != HttpStatusCode.Created) {
                error("Status ${result.status}")
            }
            Unit
        }
    }

    suspend fun register(login: String, password: String, name: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = client.post("http://10.0.2.2:8081/api/person/register") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        AuthRegisterDto(
                            email = login,
                            password = password,
                            name = name,
                            birthDate = TODO(),
                            description = TODO(),
                            avatarUrl = TODO()
                        )

                    )
                }
            }
        }
}