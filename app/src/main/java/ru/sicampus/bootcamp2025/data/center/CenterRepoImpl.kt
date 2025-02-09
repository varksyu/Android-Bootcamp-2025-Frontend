package ru.sicampus.bootcamp2025.data.center

import ru.sicampus.bootcamp2025.domain.center.CenterEntity
import ru.sicampus.bootcamp2025.domain.center.CenterRepo
import ru.sicampus.bootcamp2025.data.user.UserDto
import ru.sicampus.bootcamp2025.domain.user.UserEntity
import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource
class CenterRepoImpl(
    private val centerNetworkDataSource: CenterNetworkDataSource
) : CenterRepo {
    override suspend fun getCenters(lat : Double?, lng : Double?): Result<List<CenterEntity>> {

        return centerNetworkDataSource.getCenters().map { listDto ->
            listDto.mapNotNull { dto ->
                //Log.d("idDto", "${dto.name ?: return@mapNotNull null}")
                val id = dto.id ?: return@mapNotNull null
                val name = dto.name ?: return@mapNotNull null
                val description = dto.description ?: return@mapNotNull null
                CenterEntity(
                    id = id,
                    name = name,
                    description = description,
                    distance = dto.distance ?: -1.0,
                    lat = dto.lat ?: 0.0,
                    lng = dto.lng ?: 0.0,
                    users = dto.users?.map { userDto ->
                        userDto.toEntity()
                    } ?: emptyList()
                )
            }
        }
    }
    override suspend fun getCenter(id: Long): Result<CenterEntity> {
        return centerNetworkDataSource.getCenter(id).map { dto ->
            val idDto = dto.id ?: throw Exception("ID is null")
            val name = dto.name ?: throw Exception("name is null")
            val description = dto.description ?: throw Exception("description is null")

            CenterEntity(
                id = idDto,
                name = name,
                description = description,
                distance = dto.distance ?: -1.0,
                lat = dto.lat ?: 0.0,
                lng = dto.lng ?: 0.0,
                users = dto.users?.map { it.toEntity() } ?: emptyList()
            )
        }
    }

    override suspend fun joinCenter(name : String) : Result<Boolean> {
        return try {
            val response =
                centerNetworkDataSource.joinCenter(AuthStorageDataSource.userInfo?.id!!, name)
            Result.success(true)
        } catch (e: Exception) {
            Result.success(false)
        }
    }
}