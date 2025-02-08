package ru.sicampus.bootcamp2025.data.user

import ru.sicampus.bootcamp2025.domain.user.UserEntity
import ru.sicampus.bootcamp2025.domain.user.UserRepo

class UserRepoImpl (
    private val userNetworkDataSource: UserNetworkDataSource
) : UserRepo {
    override suspend fun getUser(id: Long?): Result<UserEntity> {
        return userNetworkDataSource.getUser(id).map { dto ->
            UserEntity(
                id = dto.id ?: -1,
                email = dto.email,
                birthDate = dto.birthDate ?: "",
                name = dto.name,
                description = dto.description ?: "",
                avatarUrl = dto.avatarUrl ?: "",
                center = dto.center ?: "",
                authorities = dto.authorities,
                centerDescription = dto.centerDescription ?: "",
                createdAt = dto.createdAt,
                joinedAt = dto.joinedAt
                )

            }
        }
    override suspend fun updateUser(userEntity: UserEntity): Result<Unit> {
        return userNetworkDataSource.updateUser(userEntity.id, UserDto(
            userEntity.id,
            email = userEntity.email,
            birthDate = userEntity.birthDate,
            name = userEntity.name,
            description = userEntity.description,
            avatarUrl = userEntity.avatarUrl,
            joinedAt = userEntity.joinedAt,
            createdAt = userEntity.createdAt,
            center = userEntity.center,
            centerDescription = userEntity.centerDescription,
            authorities = userEntity.authorities,
        )
        ).map { }
    }
}