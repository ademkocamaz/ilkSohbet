package ilkadam.ilkmuhabbet.domain.usecase.authScreen

import ilkadam.ilkmuhabbet.domain.repository.AuthScreenRepository

class SignIn(
    private val authScreenRepository: AuthScreenRepository
) {

    suspend operator fun invoke() =
        authScreenRepository.signIn()

    /*suspend operator fun invoke(email: String, password: String) =
        authScreenRepository.signIn(email, password)*/
}