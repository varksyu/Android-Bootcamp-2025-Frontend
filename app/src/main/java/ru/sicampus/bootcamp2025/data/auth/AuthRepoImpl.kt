package ru.sicampus.bootcamp2025.data.auth

import kotlinx.coroutines.delay
import ru.sicampus.bootcamp2025.data.user.UserNetworkDataSource
import ru.sicampus.bootcamp2025.domain.auth.AuthRepo

class AuthRepoImpl(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authStorageDataSource: AuthStorageDataSource
) : AuthRepo{
    override suspend fun isUserExist(login: String): Result<Boolean> {
        return authNetworkDataSource.IsUserExist(login)
    }

    override suspend fun register(login: String, password: String, name : String): Result<Unit> {
        return authNetworkDataSource.register(login, password, name)
    }

    override suspend fun login(login: String, password: String): Result<Unit> {
        val token = authStorageDataSource.updateToken(login, password)
        return authNetworkDataSource.login(token).onFailure {
            authStorageDataSource.clear()
        }

    }

}