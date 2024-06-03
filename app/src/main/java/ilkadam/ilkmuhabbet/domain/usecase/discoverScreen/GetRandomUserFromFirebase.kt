package ilkadam.ilkmuhabbet.domain.usecase.discoverScreen

import ilkadam.ilkmuhabbet.domain.repository.DiscoverScreenRepository

class GetRandomUserFromFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke() = discoverScreenRepository.getRandomUserFromFirebase()
}