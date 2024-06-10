package ilkadam.ilkmuhabbet.domain.usecase.discoverScreen

import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.repository.DiscoverScreenRepository

class DiscoverCheckChatRoomExistedFromFirebase(
    private val discoverScreenRepository: DiscoverScreenRepository
) {
    suspend operator fun invoke(acceptorUser: User) =
        discoverScreenRepository.checkChatRoomExistedFromFirebase(acceptorUser)
}