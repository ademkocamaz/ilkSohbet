package ilkadam.ilkmuhabbet.domain.usecase.chatScreen

import ilkadam.ilkmuhabbet.domain.repository.ChatScreenRepository

class BlockFriendToFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        chatScreenRepository.blockFriendToFirebase(registerUUID)
}