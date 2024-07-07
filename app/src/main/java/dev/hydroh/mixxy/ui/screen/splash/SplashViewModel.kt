package dev.hydroh.mixxy.ui.screen.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.AccountRepository
import dev.hydroh.mixxy.data.InstanceRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {
    suspend fun hasAccount() = accountRepository.hasAccount()
}
