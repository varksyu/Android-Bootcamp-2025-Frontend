package ru.sicampus.bootcamp2025.data.auth

import okhttp3.Credentials
import ru.sicampus.bootcamp2025.data.user.UserDto


object AuthStorageDataSource {
    var token: String? = null
        private set

    lateinit var userDto : UserDto

    fun updateToken(email : String, password : String) : String {
        val updateToken = Credentials.basic(email, password)
        token = updateToken
        return updateToken
    }

    fun updateUserDto(newUserDto : UserDto) {
        userDto = newUserDto
    }

    fun clear() {
        token = null
    }
}