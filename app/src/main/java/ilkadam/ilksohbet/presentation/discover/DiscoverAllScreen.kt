package ilkadam.ilksohbet.presentation.discover

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.SnackbarController
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.presentation.discover.components.AlertDialogAddFriend
import ilkadam.ilksohbet.presentation.discover.components.SearchField
import ilkadam.ilksohbet.presentation.discover.components.UserItem
import ilkadam.ilksohbet.utils.AdMobBanner

@Composable
fun DiscoverAllScreen(
    discoverAllScreenViewModel: DiscoverAllScreenViewModel = hiltViewModel(),
    discoverScreenViewModel: DiscoverScreenViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    var searchQuery by remember { mutableStateOf("") }
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

    Scaffold(
        topBar = {
            SearchField(
                searchQuery = searchQuery,
                onQueryChanged = { query ->
                    searchQuery = query
                    if (query == "") {
                        discoverAllScreenViewModel.getAllUsersFromFirebase()
                        users.value = discoverAllScreenViewModel.userList.value
                    } else {
                        users.value =
                            discoverAllScreenViewModel.userList.value.filter { user: User ->
                                user.userName.contains(query)
                            }
                    }

                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = scrollState
        ) {
            itemsIndexed(users.value) { index, user: User ->
                UserItem(
                    user = user,
                    onclick = {
                        selectedUser.value = user
                        openAlertDialog.value = true
                    }
                )
                if (index % 3 == 0) {
                    AdMobBanner()
                }

            }
        }
    }
}