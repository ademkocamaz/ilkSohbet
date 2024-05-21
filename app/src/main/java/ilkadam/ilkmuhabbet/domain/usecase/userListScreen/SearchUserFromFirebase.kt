package ilkadam.ilkmuhabbet.domain.usecase.userListScreen

import ilkadam.ilkmuhabbet.domain.repository.UserListScreenRepository

class SearchUserFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(userEmail: String) =
        userListScreenRepository.searchUserFromFirebase(userEmail)
}