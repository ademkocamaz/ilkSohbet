package ilkadam.ilkmuhabbet.domain.usecase.authScreen

import ilkadam.ilkmuhabbet.domain.repository.AuthScreenRepository

class SignUp(
    private val authScreenRepository: AuthScreenRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authScreenRepository.signUp(email, password)
}