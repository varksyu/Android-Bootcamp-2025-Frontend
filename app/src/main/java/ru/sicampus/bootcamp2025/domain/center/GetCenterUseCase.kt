package ru.sicampus.bootcamp2025.domain.center

class GetCenterUseCase(
    private val repo : CenterRepo
) {
    suspend operator fun invoke(id : Long) = repo.getCenter(id);
}

