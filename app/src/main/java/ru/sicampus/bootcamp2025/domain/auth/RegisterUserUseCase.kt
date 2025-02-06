package ru.sicampus.bootcamp2025.domain.auth

class RegisterUserUseCase(
    private val authRepo : AuthRepo
){
    suspend operator fun invoke(login : String, password : String, name : String) : Result<Unit> {
        return authRepo.register(login, password, name).mapCatching {
            authRepo.login(login, password)
        }
    }
}