package ilkadam.ilksohbet.presentation.userlist

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.SnackbarController
import ilkadam.ilksohbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilksohbet.presentation.userlist.components.AcceptPendingRequestList
import ilkadam.ilksohbet.presentation.userlist.components.PendingFriendRequestList
import ilkadam.ilksohbet.ui.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserlistScreen(
    userListViewModel: UserListViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
    val toastMessage = userListViewModel.toastMessage.value
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
    var chatRoomUUID: String? by remember { mutableStateOf(null) }
    var opponentUUID: String? by remember { mutableStateOf(null) }
    var oneSignalUserId: String? by remember { mutableStateOf(null) }
    var registerUUID: String? by remember { mutableStateOf(null) }
//    if (chatRoomUUID != null) {
//        LaunchedEffect(key1 = Unit) {
//            navController.popBackStack()
//            navController.navigate(BottomNavItem.Chat.screen_route)
//        }
//    }
    if (chatRoomUUID != null && opponentUUID != null && registerUUID != null && oneSignalUserId != null) {
        LaunchedEffect(key1 = Unit) {
            navController.popBackStack()
            navController.navigate(BottomNavItem.Chat.screen_route + "/${chatRoomUUID}" + "/${opponentUUID}" + "/${registerUUID}" + "/${oneSignalUserId}")
        }
    }

    LaunchedEffect(key1 = Unit) {
        userListViewModel.refreshingFriendList()
    }
    val acceptedFriendRequestList = userListViewModel.acceptedFriendRequestList
    val pendingFriendRequestList = userListViewModel.pendingFriendRequestList

//    var showAlertDialog by remember {
//        mutableStateOf(false)
//    }
//    if (showAlertDialog) {
//        AlertDialogChat(
//            onDismiss = { showAlertDialog = !showAlertDialog },
//            onConfirm = {
//                showAlertDialog = !showAlertDialog
//                userListViewModel.createFriendshipRegisterToFirebase(it)
//            })
//    }

    val scrollState = rememberLazyListState()
    var isRefreshing by remember { userListViewModel.isRefreshing }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            userListViewModel.refreshingFriendList()
        })

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {

        Scaffold(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { keyboardController.hide() })
                },
            //topBar = { ProfileAppBar() }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = scrollState,
                //verticalArrangement = Arrangement.Center,
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(acceptedFriendRequestList.value) { item ->
                    if (item.userName != "") {
                        AcceptPendingRequestList(item) {
                            chatRoomUUID = item.chatRoomUUID
                            registerUUID = item.registerUUID
                            opponentUUID = item.userUUID
                            oneSignalUserId = item.oneSignalUserId
                        }
                    }

                }
                items(pendingFriendRequestList.value) { item ->
                    if (item.acceptorUserName != "") {
                        PendingFriendRequestList(item, {
                            userListViewModel.acceptPendingFriendRequestToFirebase(item.registerUUID)
                            userListViewModel.refreshingFriendList()
                        }, {
                            userListViewModel.cancelPendingFriendRequestToFirebase(item.registerUUID)
                            userListViewModel.refreshingFriendList()
                        })
                    }
                }
            }

        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }

}