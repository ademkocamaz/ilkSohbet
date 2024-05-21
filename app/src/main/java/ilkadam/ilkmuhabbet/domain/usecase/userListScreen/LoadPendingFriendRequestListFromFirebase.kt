package ilkadam.ilkmuhabbet.domain.usecase.userListScreen

import ilkadam.ilkmuhabbet.domain.repository.UserListScreenRepository

class LoadPendingFriendRequestListFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke() =
        userListScreenRepository.loadPendingFriendRequestListFromFirebase()
}