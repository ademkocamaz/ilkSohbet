package ilkadam.ilkmuhabbet.domain.usecase.discoverScreen

import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.repository.DiscoverScreenRepository

class DiscoverCreateFriendListRegisterToFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke(
        chatRoomUUID: String,
        acceptorUser: User,
        requesterUser: User
    ) = discoverScreenRepository.createFriendListRegisterToFirebase(
        chatRoomUUID,
        acceptorUser,
        requesterUser
    )
}