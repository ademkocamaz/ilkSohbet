package ilkadam.ilksohbet.presentation.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.SnackbarController
import ilkadam.ilksohbet.domain.model.MessageRegister
import ilkadam.ilksohbet.domain.model.MessageStatus
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.model.UserStatus
import ilkadam.ilksohbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilksohbet.presentation.chat.chatAppBar.ChatAppBar
import ilkadam.ilksohbet.presentation.chat.chatAppBar.ProfilePictureDialog
import ilkadam.ilksohbet.presentation.chat.chatInput.ChatInput
import ilkadam.ilksohbet.presentation.chat.chatrow.ReceivedMessageRow
import ilkadam.ilksohbet.presentation.chat.chatrow.SentMessageRow
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatScreen(
    chatRoomUUID: String,
    opponentUUID: String,
    registerUUID: String,
    oneSignalUserId: String,
    chatViewModel: ChatScreenViewModel = hiltViewModel(),
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
    val toastMessage = chatViewModel.toastMessage.value
    val context = LocalContext.current
    LaunchedEffect(toastMessage, context) {
        if (toastMessage != "") {
            SnackbarController(this).showSnackbar(
                snackbarHostState, toastMessage, context.getString(
                    R.string.close
                )
            )
        }
    }
    chatViewModel.loadMessagesFromFirebase(chatRoomUUID, opponentUUID, registerUUID)
    ChatScreenContent(
        chatRoomUUID,
        opponentUUID,
        registerUUID,
        oneSignalUserId,
        chatViewModel,
        navController,
        keyboardController
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreenContent(
    chatRoomUUID: String,
    opponentUUID: String,
    registerUUID: String,
    oneSignalUserId: String,
    chatViewModel: ChatScreenViewModel,
    navController: NavHostController,
    keyboardController: SoftwareKeyboardController
) {
    val messages = chatViewModel.messages

    LaunchedEffect(key1 = Unit) {
        chatViewModel.loadOpponentProfileFromFirebase(opponentUUID)
    }
    var opponentProfileFromFirebase by remember {
        mutableStateOf(User())
    }
    opponentProfileFromFirebase = chatViewModel.opponentProfileFromFirebase.value
    val opponentName = opponentProfileFromFirebase.userName
    //val opponentSurname = opponentProfileFromFirebase.userSurName
    val opponentPictureUrl = opponentProfileFromFirebase.userProfilePictureUrl
    val opponentStatus =
        if (opponentProfileFromFirebase.status == UserStatus.ONLINE.toString()) stringResource(R.string.online) else stringResource(
            R.string.offline
        )

    var showDialog by remember {
        mutableStateOf(false)
    }
    if (showDialog) {
        ProfilePictureDialog(opponentPictureUrl) {
            showDialog = !showDialog
        }
    }

    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = messages.size)
    val messagesLoadedFirstTime = chatViewModel.messagesLoadedFirstTime.value
    val messageInserted = chatViewModel.messageInserted.value
    var isChatInputFocus by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = messagesLoadedFirstTime, messages, messageInserted) {
        if (messages.isNotEmpty()) {
            scrollState.scrollToItem(
                index = messages.size - 1
            )
        }
    }

    val imePaddingValues = PaddingValues()
    val imeBottomPadding = imePaddingValues.calculateBottomPadding().value.toInt()
    LaunchedEffect(key1 = imeBottomPadding) {
        if (messages.isNotEmpty()) {
            scrollState.scrollToItem(
                index = messages.size - 1
            )
        }
    }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            //.fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { keyboardController.hide() })
            },
        topBar = {
            ChatAppBar(
                //title = "$opponentName $opponentSurname",
                title = opponentName,
                description = opponentStatus.lowercase(),
                pictureUrl = opponentPictureUrl,
                onUserNameClick = {
                    /*Toast.makeText(
                        context,
                        context.getString(R.string.user_profile_clicked), Toast.LENGTH_SHORT
                    ).show()*/
                }, onBackArrowClick = {
                    navController.popBackStack()
                    navController.navigate(BottomNavItem.UserList.fullRoute)
                }, onUserProfilePictureClick = {
                    showDialog = true
                },
                onMoreDropDownBlockUserClick = {
                    chatViewModel.blockFriendToFirebase(registerUUID)
                    navController.navigate(BottomNavItem.UserList.fullRoute)
                }
            )
        },
        bottomBar = {
            ChatInput(
                onMessageChange = { messageContent ->
                    chatViewModel.insertMessageToFirebase(
                        chatRoomUUID,
                        messageContent,
                        registerUUID,
                        oneSignalUserId
                    )
                },
                onFocusEvent = {
                    isChatInputFocus = it
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                //.focusable()
                .padding(innerPadding)
            //.navigationBarsPadding()
            //.imePadding()

            //.weight(1f)
            //.fillMaxWidth(),
            ,
            state = scrollState
        ) {
            items(messages) { message: MessageRegister ->
                val sdf = remember {
                    SimpleDateFormat("HH:mm", Locale.ROOT)
                }

                when (message.isMessageFromOpponent) {
                    true -> {
                        ReceivedMessageRow(
                            text = message.chatMessage.message,
                            opponentName = opponentName,
                            quotedMessage = null,
                            messageTime = sdf.format(message.chatMessage.date),
                        )
                    }

                    false -> {
                        SentMessageRow(
                            text = message.chatMessage.message,
                            quotedMessage = null,
                            messageTime = sdf.format(message.chatMessage.date),
                            messageStatus = MessageStatus.valueOf(message.chatMessage.status)
                        )
                    }
                }
            }

        }
    }
}