package ilkadam.ilkmuhabbet.domain.usecase.profileScreen

import ilkadam.ilkmuhabbet.domain.repository.ProfileScreenRepository

class SetUserCreatedToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(time: Long) =
        profileScreenRepository.setUserCreatedToFirebase(time)

}