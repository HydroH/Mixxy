package dev.hydroh.mixxy.ui.screen.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.AccountRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    suspend fun loadAccount() = accountRepository.loadAccount()
}