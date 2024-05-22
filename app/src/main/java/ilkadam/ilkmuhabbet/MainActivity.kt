package ilkadam.ilkmuhabbet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import ilkadam.ilkmuhabbet.core.Constants
import ilkadam.ilkmuhabbet.domain.model.UserStatus
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.BottomNavigation
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.NavGraph
import ilkadam.ilkmuhabbet.presentation.commoncomponents.ChatSnackBar
import ilkadam.ilkmuhabbet.ui.theme.IlkMuhabbetTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity(), OSSubscriptionObserver {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = defaultViewModelProviderFactory.create(MainViewModel::class.java)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)

        // OneSignal Enable Notification
        OneSignal.addSubscriptionObserver(this)
        OneSignal.disablePush(false)

        setContent {
            IlkMuhabbetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenView()
                }
            }
        }
    }

    override fun onOSSubscriptionChanged(p0: OSSubscriptionStateChanges?) {
        if (p0!!.from.isSubscribed &&
            !p0.to.isSubscribed
        ) {
            println("Notifications Disabled!")
        }
        if (!p0.from.isSubscribed &&
            p0.to.isSubscribed
        ) {
            println("Notifications Enabled!")
        }
    }

    override fun onResume() {
        mainViewModel.setUserStatusToFirebase(UserStatus.ONLINE)
        super.onResume()
    }

    override fun onPause() {
        mainViewModel.setUserStatusToFirebase(UserStatus.OFFLINE)
        super.onPause()
    }

    override fun onDestroy() {
        mainViewModel.setUserStatusToFirebase(UserStatus.OFFLINE)
        super.onDestroy()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreenView() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                ChatSnackBar(
                    snackbarData = data
                )
            }
        },
        bottomBar = {
            bottomBarState.value =
                currentRoute != BottomNavItem.SignIn.fullRoute &&
                        currentRoute != BottomNavItem.SignUp.fullRoute &&
                        currentRoute != BottomNavItem.Chat.fullRoute
            BottomNavigation(
                navController = navController,
                bottomBarState = bottomBarState.value,
                snackbarHostState
            )
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            HorizontalPager(
//                count = items.
////                state = pagerState
//            ) {
            NavGraph(
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController!!
            )
//            }
        }
    }
}