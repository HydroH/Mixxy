package dev.hydroh.mixxy.ui.screen.login

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.hydroh.mixxy.ui.enums.LoadingState
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToTimeline: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val ctx = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (uiState.isWaitingAuth) {
                        viewModel.updateIsWaitingAuth(false)
                        viewModel.checkAuth()
                    }
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.loadingState) {
        if (uiState.loadingState == LoadingState.SUCCESS) {
            onNavigateToTimeline()
        }
    }

    Column {
        Spacer(
            Modifier
                .weight(1f)
                .animateContentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Username
            OutlinedTextField(
                value = uiState.host,
                onValueChange = { viewModel.updateHost(it) },
                enabled = uiState.loadingState != LoadingState.LOADING,
                label = {
                    Text(
                        text = "Host",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(24.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
            )
            // Submit
            Button(
                onClick = {
                    viewModel.updateIsWaitingAuth(true)
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(ctx, Uri.parse(viewModel.newAuth()))
                },
                enabled = uiState.loadingState != LoadingState.LOADING,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 30.dp),
            ) {
                if (uiState.loadingState != LoadingState.LOADING) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
