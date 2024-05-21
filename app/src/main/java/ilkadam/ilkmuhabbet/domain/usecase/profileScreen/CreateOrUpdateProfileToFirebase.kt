package ilkadam.ilkmuhabbet.domain.usecase.profileScreen

import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.repository.ProfileScreenRepository

class CreateOrUpdateProfileToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(user: User) =
        profileScreenRepository.createOrUpdateProfileToFirebase(user)
}