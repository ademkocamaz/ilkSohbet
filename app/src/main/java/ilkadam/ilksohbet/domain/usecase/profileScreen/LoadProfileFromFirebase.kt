package ilkadam.ilksohbet.domain.usecase.profileScreen

import ilkadam.ilksohbet.domain.repository.ProfileScreenRepository

class LoadProfileFromFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke() = profileScreenRepository.loadProfileFromFirebase()
}