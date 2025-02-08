package ru.sicampus.bootcamp2025.domain.user

import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource
import ru.sicampus.bootcamp2025.data.user.UserDto

class GetUserUseCase(
    private val repo: UserRepo,
    private val authStorageDataSource: AuthStorageDataSource
) {
    suspend fun getUserFromStorage() : UserDto? {
        return authStorageDataSource.userInfo
    }
    suspend operator fun invoke() = repo.getUser(getUserFromStorage()?.id)
    suspend fun updateUser(userEntity: UserEntity): Result<Unit> {
        return repo.updateUser(userEntity)
    }

}