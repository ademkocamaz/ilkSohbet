package ilkadam.ilksohbet.domain.usecase.authScreen

import ilkadam.ilksohbet.domain.repository.AuthScreenRepository

class SignIn(
    private val authScreenRepository: AuthScreenRepository
) {

    suspend operator fun invoke() =
        authScreenRepository.signIn()

    /*suspend operator fun invoke(email: String, password: String) =
        authScreenRepository.signIn(email, password)*/
}