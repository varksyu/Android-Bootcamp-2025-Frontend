package ru.sicampus.bootcamp2025.domain.user

import ru.sicampus.bootcamp2025.data.user.UserDto

interface UserRepo {
    suspend fun getUser(id: Long?) : Result<UserEntity>
    suspend fun updateUser(userEntity: UserEntity): Result<Unit>
}