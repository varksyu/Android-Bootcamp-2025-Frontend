package ru.sicampus.bootcamp2025.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id : Long?,
    @SerialName("email")
    var email: String?,
    @SerialName("birthDate")
    var birthDate : String?,
    @SerialName("name")
    var name: String,
    @SerialName("description")
    var description: String?,
    @SerialName("avatarUrl")
    var avatarUrl: String?,
    /*@SerialName("joinedAt")
    val joinedAt : Timestamp?,
    @SerialName("createdAt")
    val createdAt : Timestamp?,*/
    @SerialName("center")
    var center : String?,
    /*@SerialName("centerDescription")
    var centerDescription : String?,*/
    @SerialName("adminRights")
    val adminRights : Boolean?
)
