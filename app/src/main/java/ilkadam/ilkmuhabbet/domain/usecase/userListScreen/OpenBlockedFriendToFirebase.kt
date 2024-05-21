package ilkadam.ilkmuhabbet.domain.usecase.userListScreen

import ilkadam.ilkmuhabbet.domain.repository.UserListScreenRepository

class OpenBlockedFriendToFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        userListScreenRepository.openBlockedFriendToFirebase(registerUUID)
}