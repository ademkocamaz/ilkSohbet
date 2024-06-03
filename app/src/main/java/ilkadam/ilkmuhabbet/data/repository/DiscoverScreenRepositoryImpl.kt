package ilkadam.ilkmuhabbet.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import ilkadam.ilkmuhabbet.core.Constants
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.repository.DiscoverScreenRepository
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.random.Random

class DiscoverScreenRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : DiscoverScreenRepository {

    override suspend fun getRandomUserFromFirebase(): Flow<Response<User>> = callbackFlow {
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)
            val databaseReference = database.getReference("Profiles")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount

                    val user = snapshot.getValue<User>()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        } catch (exception: Exception) {
            this@callbackFlow.trySendBlocking(
                Response.Error(
                    exception.message ?: Constants.ERROR_MESSAGE
                )
            )
        }
    }
}