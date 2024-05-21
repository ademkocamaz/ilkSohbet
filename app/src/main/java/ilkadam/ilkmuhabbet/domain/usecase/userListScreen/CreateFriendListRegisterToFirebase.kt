package ilkadam.ilkmuhabbet.domain.usecase.userListScreen

import ilkadam.ilkmuhabbet.domain.repository.UserListScreenRepository

class CreateFriendListRegisterToFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(
        chatRoomUUID: String,
        acceptorEmail: String,
        acceptorUUID: String,
        acceptorOneSignalUserId: String
    ) = userListScreenRepository.createFriendListRegisterToFirebase(
        chatRoomUUID,
        acceptorEmail,
        acceptorUUID,
        acceptorOneSignalUserId
    )
}