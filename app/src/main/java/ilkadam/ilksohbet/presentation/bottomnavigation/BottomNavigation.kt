package ilkadam.ilksohbet.presentation.bottomnavigation

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.SnackbarController
import ilkadam.ilksohbet.presentation.bottomnavigation.components.CustomNavItem
import ilkadam.ilksohbet.presentation.userlist.UserListViewModel

@Composable
fun BottomNavigation(
    navController: NavController,
    bottomBarState: Boolean,
    snackbarHostState: SnackbarHostState,
) {
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.UserList,
        BottomNavItem.Discover,
        BottomNavItem.DiscoverAll
    )
    val userListViewModel: UserListViewModel = hiltViewModel()

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
    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomAppBar(modifier = Modifier.height(60.dp)) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
//            IconButton(onClick = {
//                navController.navigate(BottomNavItem.Profile.screen_route) {
//                    navController.graph.startDestinationRoute?.let { screen_route ->
//                        popUpTo(screen_route) {
//                            saveState = true
//                        }
//                    }
//                    launchSingleTop = true
//                    restoreState = true
//                }
//            }) {
//                if (currentRoute == BottomNavItem.Profile.screen_route) {
//                    Icon(
//                        imageVector = Icons.Filled.Person,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                } else {
//                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
//                }
//
//            }
//            IconButton(onClick = {
//                navController.navigate(BottomNavItem.UserList.screen_route) {
//                    navController.graph.startDestinationRoute?.let { screen_route ->
//                        popUpTo(screen_route) {
//                            saveState = true
//                        }
//                    }
//                    launchSingleTop = true
//                    restoreState = true
//                }
//            }) {
//                if (currentRoute == BottomNavItem.UserList.screen_route) {
//                    Icon(
//                        imageVector = Icons.Filled.Chat,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                } else {
//                    Icon(imageVector = Icons.Outlined.Chat, contentDescription = null)
//                }
//            }
            items.forEach { item ->
                CustomNavItem(
                    onClick = {
                        navController.navigate(item.screen_route) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    iconSelected = {
                        if (currentRoute == item.screen_route) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = item.icon,
                                contentDescription = item.title,
                            )
                        }
                    }
                )
            }
            /*Spacer(Modifier.weight(1f, true))
            if (currentRoute == BottomNavItem.UserList.screen_route) {
                var showAlertDialog by remember {
                    mutableStateOf(false)
                }
                if (showAlertDialog) {
                    AlertDialogChat(
                        onDismiss = { showAlertDialog = !showAlertDialog },
                        onConfirm = {
                            showAlertDialog = !showAlertDialog
                            userListViewModel.createFriendshipRegisterToFirebase(it)
                        })
                }
                ExtendedFloatingActionButton(
                    modifier = Modifier.padding(all = MaterialTheme.spacing.small),
                    onClick = {
                        showAlertDialog = !showAlertDialog
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(text = stringResource(R.string.add_new_message))
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }*/
//            items.forEach { item ->
//                NavigationBarItem(
//                    icon = {
//                        Icon(imageVector = item.icon, contentDescription = item.title)
//                    },
//                    label = {
//                        Text(item.title)
//                    },
//                    selected = currentRoute == item.screen_route,
//                    onClick = {
//                        navController.navigate(item.screen_route) {
//                            navController.graph.startDestinationRoute?.let { screen_route ->
//                                popUpTo(screen_route) {
//                                    saveState = true
//                                }
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                )
//            }
        }
    }
}