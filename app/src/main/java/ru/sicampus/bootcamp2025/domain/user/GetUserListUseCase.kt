package ru.sicampus.bootcamp2025.domain.user

import ru.sicampus.bootcamp2025.data.auth.AuthStorageDataSource
import ru.sicampus.bootcamp2025.data.user.UserDto

class GetUserListUseCase (
    private val repo: UserRepo
    ) {
        suspend operator fun invoke() = repo.getUserList();
}