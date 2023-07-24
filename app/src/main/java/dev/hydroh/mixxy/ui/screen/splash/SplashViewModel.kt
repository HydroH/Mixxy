package dev.hydroh.mixxy.ui.screen.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.remote.MisskeyDataSource
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource
) : ViewModel() {
    fun hasClient() = misskeyDataSource.client != null
}