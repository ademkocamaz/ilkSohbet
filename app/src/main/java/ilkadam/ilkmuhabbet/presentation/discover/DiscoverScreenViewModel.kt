package ilkadam.ilkmuhabbet.presentation.discover

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ilkadam.ilkmuhabbet.domain.model.User
import ilkadam.ilkmuhabbet.domain.usecase.discoverScreen.DiscoverScreenUseCases
import ilkadam.ilkmuhabbet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverScreenViewModel @Inject constructor(
    private val discoverScreenUseCases: DiscoverScreenUseCases
) : ViewModel() {
    var isLoading = mutableStateOf(false)
        private set
    var userDataStateFromFirebase = mutableStateOf(User())
        private set

    init {
        getRandomUserFromFirebase()
    }

    fun getRandomUserFromFirebase() {
        viewModelScope.launch {
            discoverScreenUseCases.getRandomUserFromFirebase().collect { response ->
                when (response) {
                    is Response.Loading -> {
                        isLoading.value = true
                    }

                    is Response.Success -> {
                        if (response.data != null) {
                            userDataStateFromFirebase.value = response.data
                        } else {
                            userDataStateFromFirebase.value = User()
                        }
                        isLoading.value = false
                    }

                    is Response.Error -> {}
                }
            }
        }
    }
}