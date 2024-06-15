package ilkadam.ilksohbet.domain.usecase.profileScreen

import ilkadam.ilksohbet.domain.model.UserStatus
import ilkadam.ilksohbet.domain.repository.ProfileScreenRepository

class SetUserStatusToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(userStatus: UserStatus) =
        profileScreenRepository.setUserStatusToFirebase(userStatus)
}