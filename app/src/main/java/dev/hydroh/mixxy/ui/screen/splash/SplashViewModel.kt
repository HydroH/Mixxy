package dev.hydroh.mixxy.ui.screen.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.AccountRepository
import dev.hydroh.mixxy.data.InstanceRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val instanceRepository: InstanceRepository,
) : ViewModel() {
    suspend fun loadAccount() = accountRepository.loadAccount()
    suspend fun loadEmojis() = instanceRepository.initEmojis()
}