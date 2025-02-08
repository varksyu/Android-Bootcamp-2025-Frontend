package ru.sicampus.bootcamp2025.data.user

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2025.data.Network.client
import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource.token

class UserNetworkDataSource {
    suspend fun getUser(id: Long?): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {

            val result = client.get("http://10.0.2.2:8081/api/user/${id}") {
                header(HttpHeaders.Authorization, token)
            }


            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}")
            }
            Log.d("result", result.bodyAsText())
            result.body()
        }
    }
    suspend fun updateUser(id: Long?, userDto: UserDto): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.put("http://10.0.2.2:8081/api/user/${id}") {
                header(HttpHeaders.Authorization, token)
                header(HttpHeaders.ContentType, "application/json")
                setBody(userDto)
            }
            Log.d("serverCode", "${result.status}")

            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}")
            }
        }
    }

    suspend fun getUserList() : Result<List<UserDto>> = withContext(Dispatchers.IO){
        runCatching {
            val result = client.get("http://10.0.2.2:8081/api/user/free") {
                header(HttpHeaders.Authorization, token)
            }
            Log.d("serverCode", "${result.status}")
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}")
            }
            result.body()
        }

    }
}