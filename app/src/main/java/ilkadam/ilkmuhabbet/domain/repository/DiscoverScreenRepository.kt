package ilkadam.ilkmuhabbet.domain.repository

import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.flow.Flow

interface DiscoverScreenRepository {
    suspend fun discover(): Flow<Response<Boolean>>
}