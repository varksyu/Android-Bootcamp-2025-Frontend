package ru.sicampus.bootcamp2025.domain.center

import android.util.Log

class GetCentersUseCase (
    private val repo : CenterRepo
) {
    suspend operator fun invoke(lat : Double? = null, lng : Double? = null) = repo.getCenters(lat, lng)

}