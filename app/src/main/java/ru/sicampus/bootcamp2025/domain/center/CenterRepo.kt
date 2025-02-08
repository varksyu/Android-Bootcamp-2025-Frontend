package ru.sicampus.bootcamp2025.domain.center

interface CenterRepo {
    suspend fun getCenters(lat : Double? = null, lng : Double? = null) : Result<List<CenterEntity>>
}