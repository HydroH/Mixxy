package dev.hydroh.mixxy.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToTimelineScreen: () -> Unit,
    onNavigateToLoginScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        if (viewModel.hasAccount()) {
            onNavigateToTimelineScreen()
        } else {
            onNavigateToLoginScreen()
        }
    }
}
