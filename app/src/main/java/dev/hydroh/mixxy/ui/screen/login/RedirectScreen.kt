package dev.hydroh.mixxy.ui.screen.login

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination
@Composable
fun RedirectScreen(
    url: String,
    resultNavigator: ResultBackNavigator<Boolean>? = null
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val ctx = LocalContext.current
    var navBack by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    navBack = true
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(navBack) {
        if (navBack) resultNavigator?.navigateBack(result = true)
    }

    LaunchedEffect(Unit) {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(ctx, Uri.parse(url))
    }
}