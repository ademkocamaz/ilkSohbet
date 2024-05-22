package ilkadam.ilkmuhabbet.presentation.auth.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ilkadam.ilkmuhabbet.R
import ilkadam.ilkmuhabbet.core.SnackbarController
import ilkadam.ilkmuhabbet.presentation.auth.AuthViewModel
import ilkadam.ilkmuhabbet.presentation.auth.components.BottomRouteSign
import ilkadam.ilkmuhabbet.presentation.auth.components.ButtonSign
import ilkadam.ilkmuhabbet.presentation.auth.components.LoginEmailCustomOutlinedTextField
import ilkadam.ilkmuhabbet.presentation.auth.components.LoginPasswordCustomOutlinedTextField
import ilkadam.ilkmuhabbet.presentation.auth.components.TextLightweight
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilkmuhabbet.ui.theme.spacing

@Composable
fun SignUpScreen(
    emailFromSignIn: String,
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {

    //Set SnackBar
    val toastMessage = authViewModel.toastMessage.value
    LaunchedEffect(key1 = toastMessage) {
        if (toastMessage != "") {
            SnackbarController(this).showSnackbar(snackbarHostState, toastMessage, "Close")
        }
    }

    //For test user information
    var textEmail: String? by remember { mutableStateOf("") }//gimli@gmail.com
    var textPassword: String? by remember { mutableStateOf("") }//123456
    LaunchedEffect(key1 = Unit) {
        textEmail = emailFromSignIn
    }

    //Sign Up Navigate
    val isUserSignUp = authViewModel.isUserSignUpState.value
    LaunchedEffect(key1 = isUserSignUp) {
        if (isUserSignUp) {
            keyboardController.hide()
            navController.navigate(BottomNavItem.Profile.fullRoute)
        }
    }

    //Compose Components
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
                .padding(MaterialTheme.spacing.large)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
                TextLightweight()

                Box(modifier = Modifier.padding(top = MaterialTheme.spacing.extraLarge)) {
                    LoginEmailCustomOutlinedTextField(textEmail!!, "Email", Icons.Default.Email) {
                        textEmail = it
                    }
                }
                Box(modifier = Modifier.padding(top = MaterialTheme.spacing.medium)) {
                    LoginPasswordCustomOutlinedTextField(
                        textPassword!!,
                        "Password",
                        Icons.Default.Password
                    ) {
                        textPassword = it
                    }
                }

                ButtonSign(
                    onclick = {
                        authViewModel.signUp(textEmail!!, textPassword!!)
                    },
                    signInOrSignUp = "Sign Up"
                )
            }
        }

        BottomRouteSign(
            modifier = Modifier
                .weight(2f)
                .fillMaxSize()
                .focusable(true)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { keyboardController.hide() })
                },
            onclick = {
                if (textEmail == "") {
                    navController.popBackStack()
                    navController.navigate(BottomNavItem.SignIn.fullRoute)
                } else {
                    navController.popBackStack()
                    navController.navigate(BottomNavItem.SignIn.screen_route + "?emailFromSignUp=$textEmail")
                }
            },
            signInOrSignUp = "Sign In",
            label = "Already have an account?"
        )
    }
}