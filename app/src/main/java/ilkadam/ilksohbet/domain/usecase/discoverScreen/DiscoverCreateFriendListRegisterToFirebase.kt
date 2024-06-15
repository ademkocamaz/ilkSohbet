package ilkadam.ilksohbet.domain.usecase.discoverScreen

import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.repository.DiscoverScreenRepository

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