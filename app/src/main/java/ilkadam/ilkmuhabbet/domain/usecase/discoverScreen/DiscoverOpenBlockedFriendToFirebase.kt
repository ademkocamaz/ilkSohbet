package ilkadam.ilkmuhabbet.domain.usecase.discoverScreen

import ilkadam.ilkmuhabbet.domain.repository.DiscoverScreenRepository

class DiscoverOpenBlockedFriendToFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        discoverScreenRepository.openBlockedFriendToFirebase(registerUUID)
}