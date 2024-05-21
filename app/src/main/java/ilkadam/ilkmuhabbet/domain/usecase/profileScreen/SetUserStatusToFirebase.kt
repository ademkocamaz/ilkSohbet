package ilkadam.ilkmuhabbet.domain.usecase.profileScreen

import ilkadam.ilkmuhabbet.domain.model.UserStatus
import ilkadam.ilkmuhabbet.domain.repository.ProfileScreenRepository

class SetUserStatusToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(userStatus: UserStatus) =
        profileScreenRepository.setUserStatusToFirebase(userStatus)
}