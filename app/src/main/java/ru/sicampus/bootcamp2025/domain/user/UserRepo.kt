package ru.sicampus.bootcamp2025.domain.user

import ru.sicampus.bootcamp2025.domain.center.CenterEntity

interface UserRepo {
    suspend fun getUser(id: Int) : Result<UserEntity>
}