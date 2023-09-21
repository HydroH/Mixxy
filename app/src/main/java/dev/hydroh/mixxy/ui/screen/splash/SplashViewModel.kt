package dev.hydroh.mixxy.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.AccountRepository
import dev.hydroh.mixxy.data.InstanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val instanceRepository: InstanceRepository,
) : ViewModel() {
    suspend fun loadAccount() = accountRepository.loadAccount()
    fun fetchEmojis() {
        viewModelScope.launch(Dispatchers.IO) {
            instanceRepository.fetchEmojis()
        }
    }
}
