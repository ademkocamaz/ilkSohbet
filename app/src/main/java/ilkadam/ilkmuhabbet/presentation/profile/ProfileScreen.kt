package ilkadam.ilkmuhabbet.presentation.profile

import android.net.Uri
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ilkadam.ilkmuhabbet.core.SnackbarController
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.model.UserStatus
import ilkadam.ilkmuhabbet.presentation.bottomnavigation.BottomNavItem
import ilkadam.ilkmuhabbet.presentation.commoncomponents.LogOutCustomText
import ilkadam.ilkmuhabbet.presentation.profile.components.ChooseProfilePicFromGallery
import ilkadam.ilkmuhabbet.presentation.profile.components.ProfileAppBar
import ilkadam.ilkmuhabbet.presentation.profile.components.ProfileTextField
import ilkadam.ilkmuhabbet.ui.theme.spacing

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {

    val toastMessage = profileViewModel.toastMessage.value
    LaunchedEffect(key1 = toastMessage) {
        if (toastMessage != "") {
            SnackbarController(this).showSnackbar(snackbarHostState, toastMessage, "Close")
        }
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    isLoading = profileViewModel.isLoading.value
    var userDataFromFirebase by remember { mutableStateOf(User()) }
    userDataFromFirebase = profileViewModel.userDataStateFromFirebase.value

    var email by remember {
        mutableStateOf("")
    }
    email = userDataFromFirebase.userEmail

    var name by remember { mutableStateOf("") }
    name = userDataFromFirebase.userName

    var surName by remember {
        mutableStateOf("")
    }
    surName = userDataFromFirebase.userSurName
    var bio by remember { mutableStateOf("") }
    bio = userDataFromFirebase.userBio

    var phoneNumber by remember { mutableStateOf("") }
    phoneNumber = userDataFromFirebase.userPhoneNumber

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight().imePadding()
    ) {
        ProfileAppBar()
        Surface(
            modifier = Modifier
                .focusable(true)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { keyboardController.hide() })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .verticalScroll(scrollState),
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
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    Text(text = email, style = MaterialTheme.typography.titleMedium)
                    ProfileTextField(
                        entry = name,
                        hint = "Full Name",
                        onChange = { name = it },
                    )
                    ProfileTextField(surName, "Surname", { surName = it })
                    ProfileTextField(bio, "About You", { bio = it })
                    ProfileTextField(
                        phoneNumber, "Phone Number", { phoneNumber = it },
                        keyboardType = KeyboardType.Phone
                    )
                    Button(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.large)
                            .fillMaxWidth(),
                        onClick = {
                            if (name != "") {
                                profileViewModel.updateProfileToFirebase(User(userName = name))
                            }
                            if (surName != "") {
                                profileViewModel.updateProfileToFirebase(User(userSurName = surName))
                            }
                            if (bio != "") {
                                profileViewModel.updateProfileToFirebase(User(userBio = bio))
                            }
                            if (phoneNumber != "") {
                                profileViewModel.updateProfileToFirebase(User(userPhoneNumber = phoneNumber))
                            }
                        },
                        enabled = updatedImage != null || name != "" || surName != "" || bio != "" || phoneNumber != ""
                    ) {
                        Text(text = "Save Profile", style = MaterialTheme.typography.titleMedium)
                    }
                    LogOutCustomText {
                        profileViewModel.setUserStatusToFirebaseAndSignOut(UserStatus.OFFLINE)
                    }
                    Spacer(modifier = Modifier.height(50.dp))

                }
            }
        }
    }
}
