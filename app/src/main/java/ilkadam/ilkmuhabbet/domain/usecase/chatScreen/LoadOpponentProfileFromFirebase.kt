package ilkadam.ilkmuhabbet.domain.usecase.chatScreen

import ilkadam.ilkmuhabbet.domain.repository.ChatScreenRepository

class LoadOpponentProfileFromFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(opponentUUID: String) =
        chatScreenRepository.loadOpponentProfileFromFirebase(opponentUUID)
}