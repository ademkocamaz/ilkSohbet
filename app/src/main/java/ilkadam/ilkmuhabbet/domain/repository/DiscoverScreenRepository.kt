package ilkadam.ilkmuhabbet.domain.repository

import ilkadam.ilkmuhabbet.domain.model.FriendListRegister
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.flow.Flow
import java.util.UUID

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