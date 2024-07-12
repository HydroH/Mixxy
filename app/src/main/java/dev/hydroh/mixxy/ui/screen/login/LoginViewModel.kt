package dev.hydroh.mixxy.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.AccountRepository
import dev.hydroh.mixxy.ui.enum.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun updateHost(host: String) {
        _uiState.update {
            it.copy(host = host)
        }
    }

    fun updateIsWaitingAuth(isWaitingAuth: Boolean) {
        _uiState.update {
            it.copy(isWaitingAuth = isWaitingAuth)
        }
    }

    fun newAuth() = accountRepository.newAuth(_uiState.value.host)

    fun checkAuth() {
        _uiState.update {
            it.copy(loadingState = LoadingState.LOADING)
        }
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    loadingState =
                    if (accountRepository.newAccount()) {
                        LoadingState.SUCCESS
                    } else {
                        LoadingState.FAIL
                    }
                )
            }
        }
    }
}

data class LoginUIState(
    val host: String = "",
    val isWaitingAuth: Boolean = false,
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)
