package ru.sicampus.bootcamp2025.domain.center

interface CenterRepo {
    suspend fun getCenters(lat : Double? = null, lng : Double? = null) : Result<List<CenterEntity>>
    suspend fun getCenter(id : Long) : Result<CenterEntity>
    suspend fun joinCenter(name : String) : Result<Boolean>
}