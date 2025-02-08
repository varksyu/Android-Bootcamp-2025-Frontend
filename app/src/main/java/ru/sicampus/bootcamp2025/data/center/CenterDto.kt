package ru.sicampus.bootcamp2025.data.center

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2025.data.user.UserDto

@Serializable
data class CenterDto(
    @SerialName("id")
    val id : Long?,
    @SerialName("name")
    val name : String?,
    @SerialName("description")
    val description : String?,
    @SerialName("distance")
    val distance : Double?,
    @SerialName("lat")
    val lat : Double?,
    @SerialName("lng")
    val lng : Double?,
    @SerialName("users")
    val users : List<UserDto>?
)