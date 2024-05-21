package ilkadam.ilkmuhabbet.domain.repository

import ilkadam.ilkmuhabbet.domain.model.FriendListRegister
import ilkadam.ilkmuhabbet.domain.model.FriendListRow
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.flow.Flow

interface UserListScreenRepository {
    suspend fun loadAcceptedFriendRequestListFromFirebase(): Flow<Response<List<FriendListRow>>>
    suspend fun loadPendingFriendRequestListFromFirebase(): Flow<Response<List<FriendListRegister>>>

    suspend fun searchUserFromFirebase(userEmail: String): Flow<Response<User?>>

    suspend fun checkChatRoomExistedFromFirebase(acceptorUUID: String): Flow<Response<String>>
    suspend fun createChatRoomToFirebase(acceptorUUID: String): Flow<Response<String>>

    suspend fun checkFriendListRegisterIsExistedFromFirebase(
        acceptorEmail: String,
        acceptorUUID: String
    ): Flow<Response<FriendListRegister>>

    suspend fun createFriendListRegisterToFirebase(
        chatRoomUUID: String,
        acceptorEmail: String,
        acceptorUUID: String,
        acceptorOneSignalUserId: String
    ): Flow<Response<Boolean>>

    suspend fun acceptPendingFriendRequestToFirebase(registerUUID: String): Flow<Response<Boolean>>
    suspend fun rejectPendingFriendRequestToFirebase(registerUUID: String): Flow<Response<Boolean>>
    suspend fun openBlockedFriendToFirebase(registerUUID: String): Flow<Response<Boolean>>
}