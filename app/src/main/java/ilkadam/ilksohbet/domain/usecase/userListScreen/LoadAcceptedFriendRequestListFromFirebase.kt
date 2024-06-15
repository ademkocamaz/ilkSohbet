package ilkadam.ilksohbet.domain.usecase.userListScreen

import ilkadam.ilksohbet.domain.repository.UserListScreenRepository

class LoadAcceptedFriendRequestListFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke() =
        userListScreenRepository.loadAcceptedFriendRequestListFromFirebase()
}