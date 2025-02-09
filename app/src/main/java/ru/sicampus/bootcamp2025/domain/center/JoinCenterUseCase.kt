package ru.sicampus.bootcamp2025.domain.center

class JoinCenterUseCase(
    private val repo : CenterRepo
) {
    suspend operator fun invoke(name : String) = repo.joinCenter(name);
}

