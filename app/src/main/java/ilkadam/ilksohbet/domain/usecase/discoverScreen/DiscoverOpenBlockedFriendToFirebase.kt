package ilkadam.ilksohbet.domain.usecase.discoverScreen

import ilkadam.ilksohbet.domain.repository.DiscoverScreenRepository

class DiscoverOpenBlockedFriendToFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        discoverScreenRepository.openBlockedFriendToFirebase(registerUUID)
}