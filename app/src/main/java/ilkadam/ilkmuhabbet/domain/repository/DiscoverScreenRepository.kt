package ilkadam.ilkmuhabbet.domain.repository

import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.flow.Flow

interface DiscoverScreenRepository {
    suspend fun getRandomUserFromFirebase(): Flow<Response<User>>
}