package dev.hydroh.mixxy.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.local.UserDataStore
import dev.hydroh.mixxy.data.remote.MisskeyDataSource
import dev.hydroh.mixxy.ui.components.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource,
    private val userDataStore: UserDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()
    val authUrl: String
        get() {
            misskeyDataSource.newClient(_uiState.value.host)
            return misskeyDataSource.client!!.authUrl!!
        }

    fun updateHost(host: String) {
        _uiState.update {
            it.copy(host = host)
        }
    }

    fun tryAuth() {
        _uiState.update {
            it.copy(loadingState = LoadingState.LOADING)
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (misskeyDataSource.client!!.auth()) {
                _uiState.update {
                    userDataStore.updateAccessToken(misskeyDataSource.client!!.accessToken!!)
                    it.copy(loadingState = LoadingState.SUCCESS)
                }
            } else {
                _uiState.update {
                    it.copy(loadingState = LoadingState.FAIL)
                }
            }
        }
    }
}

data class LoginUIState(
    val host: String = "",
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)