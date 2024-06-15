package ilkadam.ilksohbet.domain.repository

import android.net.Uri
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.model.UserStatus
import ilkadam.ilksohbet.utils.Response
import kotlinx.coroutines.flow.Flow

interface ProfileScreenRepository {
    //suspend fun signOut(): Flow<Response<Boolean>>
    suspend fun uploadPictureToFirebase(url: Uri): Flow<Response<String>>
    suspend fun createOrUpdateProfileToFirebase(user: User): Flow<Response<Boolean>>
    suspend fun loadProfileFromFirebase(): Flow<Response<User>>
    suspend fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Response<Boolean>>

    suspend fun setUserCreatedToFirebase(time: Long): Flow<Response<Boolean>>
}