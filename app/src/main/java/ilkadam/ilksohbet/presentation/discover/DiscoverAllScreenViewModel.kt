package ilkadam.ilksohbet.presentation.discover

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ilkadam.ilksohbet.domain.model.User
import ilkadam.ilksohbet.domain.usecase.discoverScreen.DiscoverScreenUseCases
import ilkadam.ilksohbet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverAllScreenViewModel @Inject constructor(
    private val discoverScreenUseCases: DiscoverScreenUseCases
) : ViewModel() {

    var isLoading = mutableStateOf(false)
        private set

    var userList = mutableStateOf<List<User>>(listOf())
        private set

    init {
        getAllUsersFromFirebase()
    }

    fun getAllUsersFromFirebase() {
        viewModelScope.launch {
            discoverScreenUseCases.getAllUsersFromFirebase().collect { response ->
                when (response) {
                    is Response.Loading -> {
                        isLoading.value = true
                    }

                    is Response.Success -> {
                        userList.value = response.data
                        isLoading.value = false
                    }

                    is Response.Error -> {
                        isLoading.value = false
                    }
                }
            }
        }
    }

}