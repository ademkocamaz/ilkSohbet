package ilkadam.ilkmuhabbet.domain.usecase.profileScreen

import ilkadam.ilkmuhabbet.domain.repository.ProfileScreenRepository

class LoadProfileFromFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke() = profileScreenRepository.loadProfileFromFirebase()
}