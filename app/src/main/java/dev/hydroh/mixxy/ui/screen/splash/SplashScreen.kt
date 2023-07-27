package dev.hydroh.mixxy.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.hydroh.mixxy.ui.screen.destinations.LoginScreenDestination
import dev.hydroh.mixxy.ui.screen.destinations.NotesScreenDestination
import dev.hydroh.mixxy.ui.screen.destinations.SplashScreenDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        if (viewModel.loadAccount()) {
            viewModel.loadEmojis()
            navigator?.navigate(NotesScreenDestination) {
                popUpTo(SplashScreenDestination) {
                    inclusive = true
                }
            }
        } else {
            navigator?.navigate(LoginScreenDestination) {
                popUpTo(SplashScreenDestination) {
                    inclusive = true
                }
            }
        }
    }
}