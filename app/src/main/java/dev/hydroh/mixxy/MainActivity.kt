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
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import dev.hydroh.mixxy.data.remote.adapter.ContextualTokenSerializer
import dev.hydroh.mixxy.data.remote.adapter.HostSelectionInterceptor
import dev.hydroh.mixxy.ui.screen.NavGraphs
import dev.hydroh.mixxy.ui.theme.MixxyTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var hostSelectionInterceptor: HostSelectionInterceptor

    @Inject lateinit var contextualTokenSerializer: ContextualTokenSerializer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MixxyTheme {
                MixxyApp()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putString("host", hostSelectionInterceptor.host)
            putString("token", contextualTokenSerializer.token)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.apply {
            hostSelectionInterceptor.host = getString("host")
            contextualTokenSerializer.token = getString("token")
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
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}
