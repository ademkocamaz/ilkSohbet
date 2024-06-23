package ilkadam.ilksohbet.presentation.discover

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.SnackbarController
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.presentation.discover.components.AlertDialogAddFriend
import ilkadam.ilksohbet.presentation.discover.components.UserItem

@Composable
fun DiscoverAllScreen(
    discoverAllScreenViewModel: DiscoverAllScreenViewModel = hiltViewModel(),
    discoverScreenViewModel: DiscoverScreenViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val users = discoverAllScreenViewModel.userList
    val scrollState = rememberLazyListState()
    val openAlertDialog = remember { mutableStateOf(false) }
    val selectedUser = remember { mutableStateOf(User()) }

    val toastMessage = discoverScreenViewModel.toastMessage.value
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

    if (openAlertDialog.value) {
        AlertDialogAddFriend(
            onDismissRequest = {
                openAlertDialog.value = false
            },
            onConfirmation = {
                openAlertDialog.value = false
                discoverScreenViewModel.createFriendshipRegisterToFirebase(selectedUser.value)
            },
            user = selectedUser.value
        )
    }

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = scrollState
        ) {
            items(users.value) { user ->
                UserItem(
                    user = user,
                    onclick = {
                        selectedUser.value = user
                        openAlertDialog.value = true
                    }
                )
            }
        }
    }
}