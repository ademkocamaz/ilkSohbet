package ilkadam.ilksohbet.domain.usecase.discoverScreen

import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.repository.DiscoverScreenRepository

class DiscoverCreateChatRoomToFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke(acceptorUser: User) =
        discoverScreenRepository.createChatRoomToFirebase(acceptorUser)
}