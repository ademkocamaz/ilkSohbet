package ilkadam.ilkmuhabbet.presentation.bottomnavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import ilkadam.ilkmuhabbet.presentation.auth.signIn.SignInScreen
import ilkadam.ilkmuhabbet.presentation.auth.signUp.SignUpScreen
import ilkadam.ilkmuhabbet.presentation.chat.ChatScreen
import ilkadam.ilkmuhabbet.presentation.discover.DiscoverScreen
import ilkadam.ilkmuhabbet.presentation.profile.ProfileScreen
import ilkadam.ilkmuhabbet.presentation.userlist.UserlistScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
    AnimatedNavHost(navController, startDestination = BottomNavItem.SignIn.fullRoute) {
        //SIGN IN SCREEN
        composable(
            BottomNavItem.SignIn.fullRoute,
            arguments = listOf(
                navArgument("emailFromSignUp") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
//                    BottomNavItem.SignUp.fullRoute ->
//                        slideIntoContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(700)
//                        )
                    else -> null
                }

            }
        ) {
            val emailFromSignUp = remember {
                it.arguments?.getString("emailFromSignUp")
            }

            SignInScreen(
                //emailFromSignUp = emailFromSignUp ?: "",
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )
        }

        /*composable(
            BottomNavItem.SignUp.fullRoute,
            arguments = listOf(
                navArgument("emailFromSignIn") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    BottomNavItem.SignIn.fullRoute ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }

            }, exitTransition = {
                when (targetState.destination.route) {
                    BottomNavItem.SignIn.fullRoute ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            }
        ) {
            val emailFromSignIn = remember {
                it.arguments?.getString("emailFromSignIn")
            }
            SignUpScreen(
                emailFromSignIn = emailFromSignIn ?: "",
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )
        }*/

        composable(
            BottomNavItem.Profile.fullRoute,
            enterTransition = {
                when (initialState.destination.route) {
                    BottomNavItem.UserList.fullRoute ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(250, easing = LinearEasing)
                        )
                    else -> null
                }

            }, exitTransition = {
                when (targetState.destination.route) {
                    BottomNavItem.UserList.fullRoute ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(250, easing = LinearEasing)
                        )
                    else -> null
                }
            }) {
            ProfileScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )
        }
        composable(
            BottomNavItem.UserList.fullRoute,
            enterTransition = {
                when (initialState.destination.route) {
                    BottomNavItem.Profile.fullRoute ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(250, easing = LinearEasing)
                        )
//                    BottomNavItem.SignIn.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
//                    BottomNavItem.SignUp.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
//                    BottomNavItem.Profile.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))

                    else -> null
                }

            }, exitTransition = {
                when (targetState.destination.route) {
                    BottomNavItem.UserList.fullRoute ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(250, easing = LinearEasing)
                        )
                    else -> null
                }
            }) {
            UserlistScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )
        }

        composable(
            BottomNavItem.Chat.fullRoute,
            arguments = listOf(
                navArgument("chatroomUUID") {
                    type = NavType.StringType
                }, navArgument("opponentUUID") {
                    type = NavType.StringType
                }, navArgument("registerUUID") {
                    type = NavType.StringType
                }, navArgument("oneSignalUserId") {
                    type = NavType.StringType
                }),
            enterTransition = {
                when (initialState.destination.route) {
//                    BottomNavItem.UserList.fullRoute -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                    else -> null
                }

            }, exitTransition = {

                when (targetState.destination.route) {
//                    BottomNavItem.UserList.fullRoute -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                    else -> null
                }
            }) {

            val chatroomUUID = remember {
                it.arguments?.getString("chatroomUUID")
            }
            val opponentUUID = remember {
                it.arguments?.getString("opponentUUID")
            }
            val registerUUID = remember {
                it.arguments?.getString("registerUUID")
            }
            val oneSignalUserId = remember {
                it.arguments?.getString("oneSignalUserId")
            }

            ChatScreen(
                chatRoomUUID = chatroomUUID ?: "",
                opponentUUID = opponentUUID ?: "",
                registerUUID = registerUUID ?: "",
                oneSignalUserId = oneSignalUserId ?: "",
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )

        }

        composable(
            route = BottomNavItem.Discover.fullRoute,
            enterTransition = {
                when(initialState.destination.route){
                    BottomNavItem.UserList.fullRoute->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(250, easing = LinearEasing)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    BottomNavItem.Discover.fullRoute ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(250, easing = LinearEasing)
                        )
                    else -> null
                }
            }

        ){
            DiscoverScreen()
        }
    }
}