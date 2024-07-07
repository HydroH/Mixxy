package dev.hydroh.mixxy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.hydroh.mixxy.ui.screen.login.LoginRoute
import dev.hydroh.mixxy.ui.screen.login.LoginScreen
import dev.hydroh.mixxy.ui.screen.splash.SplashRoute
import dev.hydroh.mixxy.ui.screen.splash.SplashScreen
import dev.hydroh.mixxy.ui.screen.timeline.TimelineRoute
import dev.hydroh.mixxy.ui.screen.timeline.TimelineScreen
import dev.hydroh.mixxy.ui.theme.MixxyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MixxyTheme {
                MixxyApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MixxyApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = SplashRoute) {
            composable<SplashRoute> {
                SplashScreen(
                    onNavigateToTimelineScreen = {
                        navController.navigate(TimelineRoute) {
                            popUpTo(SplashRoute) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToLoginScreen = {
                        navController.navigate(LoginRoute) {
                            popUpTo(SplashRoute) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<LoginRoute> {
                LoginScreen(
                    onNavigateToTimeline = {
                        navController.navigate(TimelineRoute) {
                            popUpTo(LoginRoute) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<TimelineRoute> {
                TimelineScreen()
            }
        }
    }
}
