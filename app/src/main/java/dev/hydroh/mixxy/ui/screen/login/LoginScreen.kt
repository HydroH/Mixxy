package dev.hydroh.mixxy.ui.screen.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.hydroh.mixxy.ui.components.LoadingState
import dev.hydroh.mixxy.ui.screen.destinations.RedirectScreenDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator? = null,
    resultNavigator: ResultBackNavigator<Boolean>? = null,
    resultRecipient: ResultRecipient<RedirectScreenDestination, Unit>? = null,
    loginViewModel: LoginViewModel = viewModel(),
) {
    val uiState by loginViewModel.uiState.collectAsState()

    resultRecipient?.onNavResult { result ->
        when (result) {
            is NavResult.Value -> {
                loginViewModel.tryAuth()
            }
            else -> {}
        }
    }
    LaunchedEffect(uiState.loadingState) {
        if (uiState.loadingState == LoadingState.SUCCESS) {
            resultNavigator?.navigateBack(result = true)
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
                onValueChange = { loginViewModel.updateHost(it) },
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
                onClick = { navigator?.navigate(RedirectScreenDestination(loginViewModel.authUrl)) },
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

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}