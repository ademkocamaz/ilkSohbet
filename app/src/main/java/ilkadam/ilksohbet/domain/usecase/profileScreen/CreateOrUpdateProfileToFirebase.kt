package ilkadam.ilksohbet.domain.usecase.profileScreen

import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.repository.ProfileScreenRepository

class CreateOrUpdateProfileToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(user: User) =
        profileScreenRepository.createOrUpdateProfileToFirebase(user)
}