package ilkadam.ilksohbet.presentation.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.core.SnackbarController
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilksohbet.presentation.profile.components.ChooseProfilePicFromGallery
import ilkadam.ilksohbet.presentation.profile.components.ProfileTextField
import ilkadam.ilksohbet.ui.theme.spacing

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {

    val toastMessage = profileViewModel.toastMessage.value
    val context = LocalContext.current
    LaunchedEffect(key1 = toastMessage) {
        if (toastMessage != "") {
            SnackbarController(this).showSnackbar(
                snackbarHostState,
                toastMessage,
                context.getString(R.string.close)
            )
        }
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    isLoading = profileViewModel.isLoading.value
    var userDataFromFirebase by remember { mutableStateOf(User()) }
    userDataFromFirebase = profileViewModel.userDataStateFromFirebase.value

    /*var email by remember {
        mutableStateOf("")
    }
    email = userDataFromFirebase.userEmail*/

    var name by remember { mutableStateOf("") }
    name = userDataFromFirebase.userName

    /*var surName by remember {
        mutableStateOf("")
    }
    surName = userDataFromFirebase.userSurName*/
    var bio by remember { mutableStateOf("") }
    bio = userDataFromFirebase.userBio

    /*var phoneNumber by remember { mutableStateOf("") }
    phoneNumber = userDataFromFirebase.userPhoneNumber*/

    var userDataPictureUrl by remember { mutableStateOf("") }
    userDataPictureUrl = userDataFromFirebase.userProfilePictureUrl

    val scrollState = rememberScrollState()

    var updatedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    val isUserSignOut = profileViewModel.isUserSignOutState.value
    LaunchedEffect(key1 = isUserSignOut) {
        if (isUserSignOut) {
            navController.popBackStack()
            navController.navigate(BottomNavItem.SignIn.fullRoute)
        }
    }

    Scaffold(
        //topBar = { ProfileAppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                //.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
            //.wrapContentHeight()
            //.imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        ChooseProfilePicFromGallery(userDataPictureUrl) {
                            if (it != null) {
                                profileViewModel.uploadPictureToFirebase(it)
                            }
                        }
                    }
                    //Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    //Text(text = email, style = MaterialTheme.typography.titleMedium)
                    ProfileTextField(
                        entry = name,
                        hint = stringResource(R.string.full_name),
                        onChange = { name = it },
                    )
                    /*ProfileTextField(
                        surName,
                        stringResource(R.string.surname),
                        { surName = it })*/
                    ProfileTextField(bio, stringResource(R.string.about_you), { bio = it })
                    /*ProfileTextField(
                        phoneNumber,
                        stringResource(R.string.phone_number),
                        { phoneNumber = it },
                        keyboardType = KeyboardType.Phone
                    )*/
                    Button(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.large)
                            .fillMaxWidth(),
                        onClick = {
                            if (name != "") {
                                profileViewModel.updateProfileToFirebase(User(userName = name))
                            }
                            /*if (surName != "") {
                                profileViewModel.updateProfileToFirebase(User(userSurName = surName))
                            }*/
                            if (bio != "") {
                                profileViewModel.updateProfileToFirebase(User(userBio = bio))
                            }
                            /*if (phoneNumber != "") {
                                profileViewModel.updateProfileToFirebase(User(userPhoneNumber = phoneNumber))
                            }*/
                        },
                        //enabled = updatedImage != null || name != "" || surName != "" || bio != "" || phoneNumber != ""
                        enabled = updatedImage != null || name != "" || bio != ""
                    ) {
                        Text(
                            text = stringResource(R.string.save_profile),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    /*LogOutCustomText {
                        profileViewModel.setUserStatusToFirebaseAndSignOut(UserStatus.OFFLINE)
                    }*/
                    //Spacer(modifier = Modifier.height(100.dp))

                }
            }
        }
    }


}
