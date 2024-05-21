package ilkadam.ilkmuhabbet.domain.model

data class MessageRegister(
    var chatMessage: ChatMessage,
    var isMessageFromOpponent: Boolean
)