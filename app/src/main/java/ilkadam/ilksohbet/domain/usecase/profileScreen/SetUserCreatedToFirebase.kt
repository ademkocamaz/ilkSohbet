package ilkadam.ilksohbet.domain.usecase.profileScreen

import ilkadam.ilksohbet.domain.repository.ProfileScreenRepository

class SetUserCreatedToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(time: Long) =
        profileScreenRepository.setUserCreatedToFirebase(time)

}