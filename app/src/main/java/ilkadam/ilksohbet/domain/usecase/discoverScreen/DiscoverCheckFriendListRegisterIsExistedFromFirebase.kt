package ilkadam.ilksohbet.domain.usecase.discoverScreen

import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.repository.DiscoverScreenRepository

class DiscoverCheckFriendListRegisterIsExistedFromFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke(acceptorUser: User) =
        discoverScreenRepository.checkFriendListRegisterIsExistedFromFirebase(acceptorUser)
}