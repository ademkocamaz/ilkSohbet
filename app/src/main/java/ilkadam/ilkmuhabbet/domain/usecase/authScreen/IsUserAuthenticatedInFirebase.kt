package ilkadam.ilkmuhabbet.domain.usecase.authScreen

import ilkadam.ilkmuhabbet.domain.repository.AuthScreenRepository

class IsUserAuthenticatedInFirebase(
    private val authScreenRepository: AuthScreenRepository
) {
    operator fun invoke() = authScreenRepository.isUserAuthenticatedInFirebase()
}