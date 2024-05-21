package ilkadam.ilkmuhabbet.domain.usecase.userListScreen

import ilkadam.ilkmuhabbet.domain.repository.UserListScreenRepository

class CheckChatRoomExistedFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(acceptorUUID: String) =
        userListScreenRepository.checkChatRoomExistedFromFirebase(acceptorUUID)
}