package ilkadam.ilksohbet.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ilkadam.ilksohbet.core.Constants
import ilkadam.ilksohbet.core.Constants.ERROR_MESSAGE
import ilkadam.ilksohbet.domain.model.ChatMessage
import ilkadam.ilksohbet.domain.model.FriendStatus
import ilkadam.ilksohbet.domain.model.MessageStatus
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.repository.ChatScreenRepository
import ilkadam.ilksohbet.utils.OneSignalApiService
import ilkadam.ilksohbet.utils.Response
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatScreenRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) : ChatScreenRepository {
    override suspend fun insertMessageToFirebase(
        chatRoomUUID: String,
        messageContent: String,
        registerUUID: String,
        oneSignalUserId: String,
        user: User
    ): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val userUUID = auth.currentUser?.uid
            //val userEmail = auth.currentUser?.email
            val messageUUID = UUID.randomUUID().toString()


            /*OneSignal.postNotification(
                JSONObject(
                    "{'contents': {'en':'${user.userName}: ${messageContent}'}, 'include_player_ids': ['$oneSignalUserId']}"),
                object : OneSignal.PostNotificationResponseHandler {

                    override fun onSuccess(p0: JSONObject?) {
                        println("onSuccess")
                    }

                    override fun onFailure(p0: JSONObject?) {
                        println("onFailure: " + p0.toString())
                    }
                })*/

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.onesignal.com/")
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor { chain ->
                            chain.proceed(
                                chain.request().newBuilder()
                                    .addHeader(
                                        "Authorization",
                                        "Key ${Constants.ONESIGNAL_APP_KEY}"
                                    )
                                    .addHeader("accept", "application/json")
                                    .addHeader("content-type", "application/json")
                                    .build()
                            )
                        }
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(OneSignalApiService::class.java)
            val body =
                "{'app_id':'${Constants.ONESIGNAL_APP_ID}', 'contents': {'en':'${user.userName}: ${messageContent}'}, 'include_player_ids': ['$oneSignalUserId']}"

            service.sendNotification(body)

            val message = ChatMessage(
                userUUID!!,
                messageContent,
                System.currentTimeMillis(),
                MessageStatus.RECEIVED.toString()
            )

            val databaseRefForLastMessage =
                database.reference.child("Friend_List").child(registerUUID)
                    .child("lastMessage")
            databaseRefForLastMessage.setValue(message).await() // for last message

            val databaseRefForChatMessage =
                database.reference.child("Chat_Rooms").child(chatRoomUUID)
                    .child(messageUUID)
            databaseRefForChatMessage.setValue(message).await() // for insert message

            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
            println("insertMessageToFirebase: " + e.message)
        }
    }

    override suspend fun loadMessagesFromFirebase(
        chatRoomUUID: String,
        opponentUUID: String,
        registerUUID: String
    ): Flow<Response<List<ChatMessage>>> = callbackFlow {
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)
            val userUUID = auth.currentUser?.uid

            val databaseRefForMessageStatus =
                database.getReference("Friend_List").child(registerUUID)
                    .child("lastMessage")
            val lastMessageProfileUUID =
                databaseRefForMessageStatus.child("profileUUID").get().await().value as String

            if (lastMessageProfileUUID != userUUID) {
                databaseRefForMessageStatus.child("status").setValue(MessageStatus.READ.toString())
            }
            val databaseRefForLoadMessages =
                database.getReference("Chat_Rooms").child(chatRoomUUID)

            val postListener =
                databaseRefForLoadMessages.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messageList = arrayListOf<ChatMessage>()
                        var unReadMessageKeys = listOf<String>()

                        val job2 = launch {
                            snapshot.children.forEach {
                                if (it.value?.javaClass != Boolean::class.java) {
                                    val chatMessage = it.getValue(ChatMessage::class.java)
                                    if (chatMessage != null) {
                                        messageList.add(chatMessage)

                                        if (chatMessage.profileUUID != userUUID && chatMessage.status == MessageStatus.RECEIVED.toString()) {
                                            unReadMessageKeys =
                                                unReadMessageKeys + it.key.toString()
                                        }
                                    }
                                }
                            }
                            messageList.sortBy { it.date }
                            this@callbackFlow.trySendBlocking(Response.Success(messageList))
                        }
                        job2.invokeOnCompletion {
                            for (i in unReadMessageKeys) {
                                databaseRefForLoadMessages.child(i).updateChildren(
                                    mapOf(
                                        Pair(
                                            "/status/",
                                            MessageStatus.READ
                                        )
                                    )
                                )
                            }
                        }
                        messageList.clear()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        this@callbackFlow.trySendBlocking(Response.Error(error.message))
                    }
                })
            databaseRefForLoadMessages.addValueEventListener(postListener)

            awaitClose {
                databaseRefForLoadMessages.removeEventListener(postListener)
                channel.close()
                cancel()
            }
        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun loadOpponentProfileFromFirebase(opponentUUID: String): Flow<Response<User>> =
        callbackFlow {
            try {

                this@callbackFlow.trySendBlocking(Response.Loading)

                val databaseReference =
                    database.getReference("Profiles").child(opponentUUID).child("profile")

                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        this@callbackFlow.trySendBlocking(Response.Success(user!!))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        this@callbackFlow.trySendBlocking(Response.Error(error.message))
                    }
                })
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error(e.message ?: ERROR_MESSAGE))
            }

            awaitClose {
                channel.close()
                cancel()
            }
        }

    override suspend fun blockFriendToFirebase(registerUUID: String): Flow<Response<Boolean>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)

                val myUUID = auth.currentUser?.uid

                val databaseReference =
                    database.getReference("Friend_List").child(registerUUID)

                val childUpdates = mutableMapOf<String, Any>()
                childUpdates["/status/"] = FriendStatus.BLOCKED.toString()
                childUpdates["/blockedby/"] = myUUID.toString()

                databaseReference.updateChildren(childUpdates).await()

                this@callbackFlow.trySendBlocking(Response.Success(true))

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error(e.message ?: ERROR_MESSAGE))
            }

            awaitClose {
                channel.close()
                cancel()
            }
        }
}