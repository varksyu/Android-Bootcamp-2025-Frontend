package ru.sicampus.bootcamp2025.domain.auth

import android.util.Log

class LoginUseCase(
    private val authRepo : AuthRepo
){
    suspend operator fun invoke(email : String, password : String) : Result<Unit> {
        return authRepo.login(email, password)
    }
}