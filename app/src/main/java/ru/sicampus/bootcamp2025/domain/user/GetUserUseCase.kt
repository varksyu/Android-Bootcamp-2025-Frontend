package ru.sicampus.bootcamp2025.domain.user

class GetUserUseCase(
    private val repo: UserRepo
) {
    suspend operator fun invoke() = repo.getUser(1)

}