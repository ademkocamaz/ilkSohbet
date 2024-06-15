package ilkadam.ilksohbet.presentation.auth.signIn

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.SnackbarController
import ilkadam.ilksohbet.presentation.auth.AuthViewModel
import ilkadam.ilksohbet.presentation.auth.components.ButtonSign
import ilkadam.ilksohbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilksohbet.ui.theme.spacing

@Composable
fun SignInScreen(
    //emailFromSignUp: String,
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
    //Set SnackBar
    val snackbar = authViewModel.toastMessage.value
    val context = LocalContext.current
    LaunchedEffect(snackbar, context) {
        if (snackbar != "") {
            SnackbarController(this).showSnackbar(
                snackbarHostState, snackbar,
                context.getString(R.string.close)
            )
        }
    }

    //For test user information
    /*var textEmail: String? by remember { mutableStateOf("") }//gimli@gmail.com
    var textPassword: String? by remember { mutableStateOf("") }//123456

    LaunchedEffect(key1 = Unit) {
        textEmail = emailFromSignUp
    }*/

    //Check User Authenticated
    val isUserAuthenticated = authViewModel.isUserAuthenticatedState.value
    LaunchedEffect(Unit) {
        if (isUserAuthenticated) {
            navController.navigate(BottomNavItem.UserList.fullRoute)
        }
    }

    //Sign In Navigate
    val isUserSignIn = authViewModel.isUserSignInState.value
    LaunchedEffect(key1 = isUserSignIn) {
        if (isUserSignIn) {
            keyboardController.hide()
            navController.navigate(BottomNavItem.Profile.fullRoute)
        }
    }
    Column {
        Surface(
            modifier = Modifier
                .weight(8f)
                .fillMaxSize()
                .focusable(true)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController.hide()
                    })
                }
                .padding(MaterialTheme.spacing.large)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = stringResource(R.string.welcome), fontSize = 30.sp)
                /*Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )*/
                //TextLightweight()

                /*Box(modifier = Modifier.padding(top = MaterialTheme.spacing.extraLarge)) {
                    LoginEmailCustomOutlinedTextField(textEmail!!,
                        stringResource(R.string.email), Icons.Default.Email) {
                        textEmail = it
                    }
                }

                Box(modifier = Modifier.padding(top = MaterialTheme.spacing.medium)) {
                    LoginPasswordCustomOutlinedTextField(
                        textPassword!!,
                        stringResource(R.string.password),
                        Icons.Default.Password
                    ) {
                        textPassword = it
                    }
                }*/

                ButtonSign(
                    onclick = {
                        authViewModel.signIn()
                        //authViewModel.signIn(textEmail!!, textPassword!!)
                    },
                    signInOrSignUp = stringResource(R.string.sign_up)
                )
            }
        }
        /*BottomRouteSign(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .focusable(true)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { keyboardController.hide() })
                },
            onclick = {
                if (textEmail == "") {
                    navController.popBackStack()
                    navController.navigate(BottomNavItem.SignUp.fullRoute)
                } else {
                    navController.popBackStack()
                    navController.navigate(BottomNavItem.SignUp.screen_route + "?emailFromSignIn=$textEmail")
                }
            },
            signInOrSignUp = stringResource(R.string.sign_up),
            label = stringResource(R.string.don_t_have_an_account)
        )*/
    }
}