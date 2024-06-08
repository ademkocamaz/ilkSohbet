package ilkadam.ilkmuhabbet.domain.repository

import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DiscoverScreenRepository {
    suspend fun getRandomUserFromFirebase(): Flow<Response<User?>>
}