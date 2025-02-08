package ru.sicampus.bootcamp2025.data.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2025.domain.user.UserEntity
import java.sql.Timestamp

@Serializable
data class UserDto(
    @SerialName("id")
    val id : Long?,
    @SerialName("email")
    var email: String,
    @SerialName("birthDate")
    var birthDate : String?,
    @SerialName("name")
    var name: String,
    @SerialName("description")
    var description: String?,
    @SerialName("avatarUrl")
    var avatarUrl: String?,
    @SerialName("joinedAt")
    val joinedAt: String?,
    @SerialName("createdAt")
    val createdAt: String,
    var center : String?,
    @SerialName("centerDescription")
    var centerDescription : String?,
    @SerialName("authorities")
    val authorities : String
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = id ?: throw IllegalArgumentException("User ID cannot be null"),
            email = email,
            birthDate = birthDate,
            name = name,
            description = description,
            avatarUrl = avatarUrl,
            joinedAt = joinedAt,
            createdAt = createdAt,
            center = center,
            centerDescription = centerDescription,
            authorities = authorities
        )
    }
}
