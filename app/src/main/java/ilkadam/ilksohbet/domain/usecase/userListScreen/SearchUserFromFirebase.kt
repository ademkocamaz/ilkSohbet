package ilkadam.ilksohbet.domain.usecase.userListScreen

import ilkadam.ilksohbet.domain.repository.UserListScreenRepository

class SearchUserFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(userEmail: String) =
        userListScreenRepository.searchUserFromFirebase(userEmail)
}