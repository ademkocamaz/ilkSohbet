package ilkadam.ilksohbet.utils

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OneSignalApiService {
    @POST("/notifications?c=push")
    suspend fun sendNotification(@Body body: String)
}