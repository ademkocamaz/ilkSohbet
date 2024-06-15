package ilkadam.ilksohbet.presentation.auth

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ilkadam.ilksohbet.R
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.model.UserStatus
import ilkadam.ilksohbet.domain.usecase.authScreen.AuthUseCases
import ilkadam.ilksohbet.domain.usecase.profileScreen.ProfileScreenUseCases
import ilkadam.ilksohbet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val profileScreenUseCases: ProfileScreenUseCases,
    private val application: Application
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

    fun signIn() {
        viewModelScope.launch {
            authUseCases.signIn().collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }
                    is Response.Success -> {
                        setUserStatusToFirebase(UserStatus.ONLINE)
                        setUserCreatedToFirebase(System.currentTimeMillis())
                        isUserSignInState.value = response.data
                        toastMessage.value = application.getString(R.string.login_successful)

                    }
                    is Response.Error -> {
                        toastMessage.value = application.getString(R.string.login_failed)
                    }
                }
            }
        }
    }

    private fun setUserCreatedToFirebase(time:Long) {
        viewModelScope.launch {
            profileScreenUseCases.setUserCreatedToFirebase(time).collect{ response->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Error -> {}
                }
            }
        }
    }

    /*fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.signIn(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }
                    is Response.Success -> {
                        setUserStatusToFirebase(UserStatus.ONLINE)
                        isUserSignInState.value = response.data
                        toastMessage.value = application.getString(R.string.login_successful)

                    }
                    is Response.Error -> {
                        toastMessage.value = application.getString(R.string.login_failed)
                    }
                }
            }
        }
    }*/

    /*fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.signUp(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }
                    is Response.Success -> {
                        isUserSignUpState.value = response.data
                        toastMessage.value = application.getString(R.string.sign_up_successful)
                        firstTimeCreateProfileToFirebase()
                    }
                    is Response.Error -> {
                        try {
                            toastMessage.value = application.getString(R.string.sign_up_failed)
                        }catch (e: Exception){
                            Log.e("TAG", "signUp: ", Throwable(e))
                        }
//                        Timber.tag("TAG").e("signUp: ")
                    }
                }
            }
        }
    }*/

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
                            toastMessage.value = application.getString(R.string.profile_updated)
                        } else {
                            toastMessage.value = application.getString(R.string.profile_saved)
                        }
                    }
                    is Response.Error -> {
                        toastMessage.value = application.getString(R.string.update_failed)
                    }
                }
            }
        }
    }

}