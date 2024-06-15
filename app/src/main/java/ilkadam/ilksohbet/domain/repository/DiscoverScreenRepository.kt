package ilkadam.ilksohbet.domain.repository

import ilkadam.ilksohbet.domain.model.FriendListRegister
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.utils.Response
import kotlinx.coroutines.flow.Flow

interface DiscoverScreenRepository {
    suspend fun getRandomUserFromFirebase(): Flow<Response<User?>>

    suspend fun checkChatRoomExistedFromFirebase(acceptorUser: User): Flow<Response<String>>
    suspend fun createChatRoomToFirebase(acceptorUser: User): Flow<Response<String>>

    suspend fun checkFriendListRegisterIsExistedFromFirebase(acceptorUser: User): Flow<Response<FriendListRegister?>>

    suspend fun createFriendListRegisterToFirebase(
        chatRoomUUID: String,
        acceptorUser: User,
        requesterUser: User
    ): Flow<Response<Boolean>>

    suspend fun openBlockedFriendToFirebase(registerUUID: String): Flow<Response<Boolean>>
}