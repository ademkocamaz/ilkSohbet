package ilkadam.ilksohbet.domain.usecase.chatScreen

import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.repository.ChatScreenRepository

class InsertMessageToFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(
        chatRoomUUID: String,
        messageContent: String,
        registerUUID: String,
        oneSignalUserId: String,
        user: User
    ) = chatScreenRepository.insertMessageToFirebase(
        chatRoomUUID,
        messageContent,
        registerUUID,
        oneSignalUserId,
        user
    )
}