package ilkadam.ilkmuhabbet

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import ilkadam.ilkmuhabbet.core.AppKeyboardFocusManager
import ilkadam.ilkmuhabbet.core.Constants
import ilkadam.ilkmuhabbet.domain.model.UserStatus
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.BottomNavigation
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.NavGraph
import ilkadam.ilkmuhabbet.presentation.commoncomponents.ChatSnackBar
import ilkadam.ilkmuhabbet.presentation.splash.SplashViewModel
import ilkadam.ilkmuhabbet.ui.theme.IlkMuhabbetTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity(), OSSubscriptionObserver {
    private val splashViewModel: SplashViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen().apply {
            setOnExitAnimationListener { viewProvider ->
                ObjectAnimator.ofFloat(
                    viewProvider.view,
                    "scaleX",
                    0.5f, 0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 300
                    doOnEnd { viewProvider.remove() }
                    start()
                }
                ObjectAnimator.ofFloat(
                    viewProvider.view,
                    "scaleY",
                    0.5f, 0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 300
                    doOnEnd { viewProvider.remove() }
                    start()
                }
            }
            /*setKeepOnScreenCondition {
                splashViewModel.isSplashShow.value
            }*/
        }
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isSplashShow.value
        }

        /*setContent {
            IlkMuhabbetTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(id = R.string.app_name), fontSize = 50.sp)
                    CircularProgressIndicator()
                }
            }
        }*/

        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        //mainViewModel = defaultViewModelProviderFactory.create(MainViewModel::class.java)

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
            AppKeyboardFocusManager()
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
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
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
        /*topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },*/
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
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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