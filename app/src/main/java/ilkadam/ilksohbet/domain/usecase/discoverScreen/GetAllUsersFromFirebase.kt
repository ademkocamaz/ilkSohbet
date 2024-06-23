package ilkadam.ilksohbet.domain.usecase.discoverScreen

import ilkadam.ilksohbet.domain.repository.DiscoverScreenRepository

class GetAllUsersFromFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke() = discoverScreenRepository.getAllUsersFromFirebase()
}