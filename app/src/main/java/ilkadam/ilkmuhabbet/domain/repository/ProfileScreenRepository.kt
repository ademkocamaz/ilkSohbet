package ilkadam.ilkmuhabbet.domain.repository

import android.net.Uri
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.model.UserStatus
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.flow.Flow

interface ProfileScreenRepository {
    //suspend fun signOut(): Flow<Response<Boolean>>
    suspend fun uploadPictureToFirebase(url: Uri): Flow<Response<String>>
    suspend fun createOrUpdateProfileToFirebase(user: User): Flow<Response<Boolean>>
    suspend fun loadProfileFromFirebase(): Flow<Response<User>>
    suspend fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Response<Boolean>>
}