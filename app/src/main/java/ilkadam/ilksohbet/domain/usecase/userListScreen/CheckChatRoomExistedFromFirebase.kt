package ilkadam.ilksohbet.domain.usecase.userListScreen

import ilkadam.ilksohbet.domain.repository.UserListScreenRepository

class CheckChatRoomExistedFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(acceptorUUID: String) =
        userListScreenRepository.checkChatRoomExistedFromFirebase(acceptorUUID)
}