package ilkadam.ilksohbet.presentation.chat

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.domain.model.MessageRegister
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.usecase.chatScreen.ChatScreenUseCases
import ilkadam.ilksohbet.domain.usecase.profileScreen.ProfileScreenUseCases
import ilkadam.ilksohbet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val chatScreenUseCases: ChatScreenUseCases,
    private val profileScreenUseCases: ProfileScreenUseCases,
    private val application: Application
) : ViewModel() {
    var opponentProfileFromFirebase = mutableStateOf(User())
        private set

    var messageInserted = mutableStateOf(false)
        private set

    var messages: List<MessageRegister> by mutableStateOf(listOf())
        private set

    var messagesLoadedFirstTime = mutableStateOf(false)
        private set

    var toastMessage = mutableStateOf("")
        private set

    private var requesterUser = mutableStateOf(User())

    init {
        loadProfileFromFirebase()
    }
    private fun loadProfileFromFirebase() {
        viewModelScope.launch {
            profileScreenUseCases.loadProfileFromFirebase().collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        requesterUser.value = response.data
                    }
                    is Response.Error -> {}
                }
            }
        }
    }

    fun insertMessageToFirebase(
        chatRoomUUID: String,
        messageContent: String,
        registerUUID: String,
        oneSignalUserId: String
    ) {
        viewModelScope.launch {
            chatScreenUseCases.insertMessageToFirebase(
                chatRoomUUID,
                messageContent,
                registerUUID,
                oneSignalUserId,
                requesterUser.value
            ).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        messageInserted.value = false
                    }

                    is Response.Success -> {
                        messageInserted.value = true
                    }

                    is Response.Error -> {

                    }
                }
            }
        }
    }

    fun loadMessagesFromFirebase(chatRoomUUID: String, opponentUUID: String, registerUUID: String) {
        viewModelScope.launch {
            chatScreenUseCases.loadMessageFromFirebase(chatRoomUUID, opponentUUID, registerUUID)
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Success -> {
                            messages = listOf()
                            for (i in response.data) {
                                if (i.profileUUID == opponentUUID) {
                                    messages =
                                        messages + MessageRegister(i, true) //Opponent Message
                                } else {
                                    messages = messages + MessageRegister(i, false) //User Message
                                }

                            }
                            messagesLoadedFirstTime.value = true
                        }

                        is Response.Error -> {}
                    }
                }
        }
    }

    fun loadOpponentProfileFromFirebase(opponentUUID: String) {
        viewModelScope.launch {
            chatScreenUseCases.opponentProfileFromFirebase(opponentUUID).collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        opponentProfileFromFirebase.value = response.data
                    }

                    is Response.Error -> {}
                }
            }
        }
    }

    fun blockFriendToFirebase(registerUUID: String) {
        viewModelScope.launch {
            chatScreenUseCases.blockFriendToFirebase.invoke(registerUUID).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }

                    is Response.Success -> {
                        toastMessage.value = application.getString(R.string.friend_blocked)
                    }

                    is Response.Error -> {}
                }
            }
        }
    }
}