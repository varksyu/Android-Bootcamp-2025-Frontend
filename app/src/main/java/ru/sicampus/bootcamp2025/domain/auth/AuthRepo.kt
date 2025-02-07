package ru.sicampus.bootcamp2025.domain.auth

interface AuthRepo {
    suspend fun isUserExist(email: String): Result<Boolean?>
    suspend fun register(email: String, password: String, name : String) : Result<Unit>
    suspend fun login(email: String, password: String) : Result <Unit>
}
