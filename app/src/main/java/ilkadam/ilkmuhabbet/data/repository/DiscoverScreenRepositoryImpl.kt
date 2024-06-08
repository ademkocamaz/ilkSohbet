package ilkadam.ilkmuhabbet.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import ilkadam.ilkmuhabbet.core.Constants
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.repository.DiscoverScreenRepository
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

class DiscoverScreenRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : DiscoverScreenRepository {

    override suspend fun getRandomUserFromFirebase(): Flow<Response<User?>> = callbackFlow {
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)
            val databaseReference = database.getReference("Profiles")
            var user: User?
            databaseReference.get()
                .addOnSuccessListener { snapshot ->
                    val job = launch {
                        /*val min = System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000;
                        val max = System.currentTimeMillis()*/
                        user = snapshot.children.toList().random().child("profile").getValue<User>()
                        this@callbackFlow.trySendBlocking(
                            Response.Success(
                                user
                            )
                        )


                        /*for (user in snapshot.children) {
                            val created = user.child("created").value.toString().toLong()

                            created?.let {
                                if (created > min && created < max) {
                                    this@callbackFlow.trySendBlocking(
                                        Response.Success(
                                            user.child("profile").getValue<User>()
                                        )
                                    )
                                } else {
                                    this@callbackFlow.trySendBlocking(
                                        Response.Success(null)
                                    )
                                }
                            }

                        }*/
                    }

                }
                .addOnFailureListener {
                    this@callbackFlow.trySendBlocking(
                        Response.Error(
                            it.message ?: Constants.ERROR_MESSAGE
                        )
                    )
                }
            awaitClose {
                channel.close()
                cancel()
            }
        } catch (exception: Exception) {
            this@callbackFlow.trySendBlocking(
                Response.Error(
                    exception.message ?: Constants.ERROR_MESSAGE
                )
            )
        }
    }

    /*private fun getRandomTimestamp(min: Long, max: Long) =
        floor(Math.random() * (max - min)) + min*/

}