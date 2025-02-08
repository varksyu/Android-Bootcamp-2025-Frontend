package ru.sicampus.bootcamp2025.domain.center

import ru.sicampus.bootcamp2025.domain.user.UserEntity

data class CenterEntity(
    val id: Long,
    val name: String,
    val description: String,
    val distance: Double?,
    val lat: Double?,
    val lng: Double?,
    val users: List<UserEntity>
)