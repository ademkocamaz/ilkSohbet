package ilkadam.ilkmuhabbet.presentation.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.model.UserStatus
import ilkadam.ilkmuhabbet.domain.usecase.authScreen.AuthUseCases
import ilkadam.ilkmuhabbet.domain.usecase.profileScreen.ProfileScreenUseCases
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val profileScreenUseCases: ProfileScreenUseCases
) : ViewModel() {
    var isUserAuthenticatedState = mutableStateOf(false)
        private set

    var isUserSignInState = mutableStateOf(false)
        private set

    var isUserSignUpState = mutableStateOf(false)
        private set

    var toastMessage = mutableStateOf("")
        private set

    init {
        isUserAuthenticated()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.signIn(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }
                    is Response.Success -> {
                        setUserStatusToFirebase(UserStatus.ONLINE)
                        isUserSignInState.value = response.data
                        toastMessage.value = "Login Successful"
                    }
                    is Response.Error -> {
                        toastMessage.value = "Login Failed"
                    }
                }
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.signUp(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }
                    is Response.Success -> {
                        isUserSignUpState.value = response.data
                        toastMessage.value = "Sign Up Successful"
                        firstTimeCreateProfileToFirebase()
                    }
                    is Response.Error -> {
                        try {
                            toastMessage.value = "Sign Up Failed"
                        }catch (e: Exception){
                            Log.e("TAG", "signUp: ", Throwable(e))
                        }
//                        Timber.tag("TAG").e("signUp: ")
                    }
                }
            }
        }
    }

    private fun isUserAuthenticated() {
        viewModelScope.launch {
            authUseCases.isUserAuthenticated().collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        isUserAuthenticatedState.value = response.data
                        if (response.data) {
                            setUserStatusToFirebase(UserStatus.ONLINE)
                        }
                    }
                    is Response.Error -> {}
                }
            }
        }
    }

    private fun setUserStatusToFirebase(userStatus: UserStatus) {
        viewModelScope.launch {
            profileScreenUseCases.setUserStatusToFirebase(userStatus).collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Error -> {}
                }
            }
        }
    }

    private fun firstTimeCreateProfileToFirebase() {
        viewModelScope.launch {
            profileScreenUseCases.createOrUpdateProfileToFirebase(User()).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }
                    is Response.Success -> {
                        if (response.data) {
                            toastMessage.value = "Profile Updated"
                        } else {
                            toastMessage.value = "Profile Saved"
                        }
                    }
                    is Response.Error -> {
                        toastMessage.value = "Update Failed"
                    }
                }
            }
        }
    }

}