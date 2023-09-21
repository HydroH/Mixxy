package dev.hydroh.mixxy.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.AccountRepository
import dev.hydroh.mixxy.data.InstanceRepository
import dev.hydroh.mixxy.ui.enums.LoadingState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val instanceRepository: InstanceRepository,
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

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchEmojis() {
        GlobalScope.launch(Dispatchers.IO) {
            instanceRepository.fetchEmojis()
        }
    }
}

data class LoginUIState(
    val host: String = "",
    val isWaitingAuth: Boolean = false,
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)
