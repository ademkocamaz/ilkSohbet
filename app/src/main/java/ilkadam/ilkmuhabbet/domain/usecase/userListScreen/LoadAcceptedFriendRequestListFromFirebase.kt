package ilkadam.ilkmuhabbet.domain.usecase.userListScreen

import ilkadam.ilkmuhabbet.domain.repository.UserListScreenRepository

class LoadAcceptedFriendRequestListFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke() =
        userListScreenRepository.loadAcceptedFriendRequestListFromFirebase()
}