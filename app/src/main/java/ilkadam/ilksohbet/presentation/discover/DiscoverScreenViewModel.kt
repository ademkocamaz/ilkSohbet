package ilkadam.ilksohbet.presentation.discover

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.Constants
import ilkadam.ilksohbet.domain.model.FriendStatus
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverScreenUseCases
import ilkadam.ilksohbet.domain.usecase.profileScreen.ProfileScreenUseCases
import ilkadam.ilksohbet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverScreenViewModel @Inject constructor(
    private val discoverScreenUseCases: DiscoverScreenUseCases,
    private val profileScreenUseCases: ProfileScreenUseCases,
    private val application: Application
) : ViewModel() {
    var isLoading = mutableStateOf(false)
        private set
    var userDataStateFromFirebase = mutableStateOf(User())
        private set
    var toastMessage = mutableStateOf("")
        private set

    private var requesterUser = mutableStateOf(User())

    init {
        getRandomUserFromFirebase()
        loadProfileFromFirebase()
    }

    fun getRandomUserFromFirebase() {
        viewModelScope.launch {
            discoverScreenUseCases.getRandomUserFromFirebase().collect { response ->
                when (response) {
                    is Response.Loading -> {
                        isLoading.value = true
                    }

                    is Response.Success -> {
                        if (response.data != null) {
                            userDataStateFromFirebase.value = response.data
                        } else {
                            userDataStateFromFirebase.value = User()
                        }
                        isLoading.value = false
                    }

                    is Response.Error -> {
                        toastMessage.value = response.message
                        isLoading.value = false
                    }
                }
            }
        }
    }

    private fun loadProfileFromFirebase() {
        viewModelScope.launch {
            profileScreenUseCases.loadProfileFromFirebase().collect { response ->
                when (response) {
                    is Response.Loading -> {
                        isLoading.value = true
                    }

                    is Response.Success -> {
                        requesterUser.value = response.data
                        //delay(500)
                        isLoading.value = false
                    }

                    is Response.Error -> {}
                }
            }
        }
    }

    fun createFriendshipRegisterToFirebase(
        acceptorUser: User
    ) {
        viewModelScope.launch {
            checkChatRoomExistFromFirebaseAndCreateIfNot(
                acceptorUser
            )
        }
    }

    private fun checkChatRoomExistFromFirebaseAndCreateIfNot(
        acceptorUser: User
    ) {
        viewModelScope.launch {
            discoverScreenUseCases.discoverCheckChatRoomExistedFromFirebase(acceptorUser)
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Success -> {
                            if (response.data == Constants.NO_CHATROOM_IN_FIREBASE_DATABASE) {
                                //Log.i("response.data", Constants.NO_CHATROOM_IN_FIREBASE_DATABASE)
                                createChatRoomToFirebase(
                                    acceptorUser
                                )
                            } else {
                                //Log.i("response.data", "else")
                                checkFriendListRegisterIsExistFromFirebase(
                                    response.data,
                                    acceptorUser
                                )
                            }
                        }

                        is Response.Error -> {}
                    }

                }
        }
    }

    private fun createChatRoomToFirebase(
        acceptorUser: User
    ) {
        viewModelScope.launch {
            discoverScreenUseCases.discoverCreateChatRoomToFirebase(acceptorUser)
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Success -> {
                            //ChatRoom created.
                            checkFriendListRegisterIsExistFromFirebase(response.data, acceptorUser)
                        }

                        is Response.Error -> {}
                    }
                }
        }
    }

    private fun checkFriendListRegisterIsExistFromFirebase(
        chatRoomUUID: String,
        acceptorUser: User
    ) {

        viewModelScope.launch {
            discoverScreenUseCases.discoverCheckFriendListRegisterIsExistedFromFirebase(acceptorUser)
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            toastMessage.value = ""
                        }

                        is Response.Success -> {
                            //Log.i("discoverCheckFriendListRegisterIsExistedFromFirebase", "Response.Success")
                            //Log.i("discoverCheckFriendListRegisterIsExistedFromFirebase", response.data.toString())
                            if (response.data == null) {
                                toastMessage.value =
                                    application.getString(R.string.friend_request_sent)
                                createFriendListRegisterToFirebase(
                                    chatRoomUUID,
                                    acceptorUser,
                                    requesterUser.value
                                )
                            } else if (response.data.status == FriendStatus.PENDING.toString()) {
                                toastMessage.value =
                                    application.getString(R.string.already_have_friend_request)

                            } else if (response.data.status == FriendStatus.ACCEPTED.toString()) {
                                toastMessage.value =
                                    application.getString(R.string.you_are_already_friend)
                            } else if (response.data.status == FriendStatus.BLOCKED.toString()) {
                                openBlockedFriendToFirebase(response.data.registerUUID)
                            }
                        }

                        is Response.Error -> {}
                    }
                }
        }
    }

    private fun createFriendListRegisterToFirebase(
        chatRoomUUID: String,
        acceptorUser: User,
        requesterUser: User
    ) {
        viewModelScope.launch {
            discoverScreenUseCases.discoverCreateFriendListRegisterToFirebase(
                chatRoomUUID,
                acceptorUser,
                requesterUser
            )
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            //Log.i("discoverCreateFriendListRegisterToFirebase", "response.loading")
                        }

                        is Response.Success -> {
                            //Log.i("discoverCreateFriendListRegisterToFirebase", "response.success")
                        }

                        is Response.Error -> {
                            //Log.i("discoverCreateFriendListRegisterToFirebase", "response.error")
                        }
                    }
                }
        }
    }

    private fun openBlockedFriendToFirebase(registerUUID: String) {
        viewModelScope.launch {
            discoverScreenUseCases.discoverOpenBlockedFriendToFirebase(registerUUID)
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Success -> {
                            if (response.data) {
                                toastMessage.value =
                                    application.getString(R.string.user_block_opened_and_accept_as_friend)
                            } else {
                                toastMessage.value =
                                    application.getString(R.string.you_are_blocked_by_user)
                            }
                        }

                        is Response.Error -> {}
                    }

                }
        }
    }
}