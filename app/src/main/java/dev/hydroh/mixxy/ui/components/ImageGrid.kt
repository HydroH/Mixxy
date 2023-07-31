package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.origeek.imageViewer.previewer.ImagePreviewer
import com.origeek.imageViewer.previewer.TransformImageView
import com.origeek.imageViewer.previewer.rememberPreviewerState
import dev.hydroh.misskey.client.entity.DriveFile
import kotlinx.coroutines.launch

@Composable
fun ImageGrid(
    files: List<DriveFile>,
    modifier: Modifier = Modifier,
    gridId: String = "",
) {
    val previewerState =
        rememberPreviewerState(enableVerticalDrag = true) { index ->
            gridId + files[index].id
        }
    val scope = rememberCoroutineScope()

    when (files.count()) {
        1 -> {
            Box(modifier = modifier.padding(2.dp)) {
                TransformImageView(
                    key = gridId + files[0].id,
                    painter = rememberAsyncImagePainter(model = files[0].thumbnailUrl),
                    previewerState = previewerState,
                    modifier = Modifier.clickable {
                        scope.launch { previewerState.openTransform(0) }
                    }
                )
            }
        }

        2, 3, 4 -> {
            VerticalGrid(
                columns = 2,
                itemCount = files.count(),
                contentPadding = PaddingValues(2.dp),
                modifier = modifier,
            ) { index ->
                TransformImageView(
                    key = gridId + files[index].id,
                    painter = rememberAsyncImagePainter(model = files[index].thumbnailUrl),
                    previewerState = previewerState,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures {
                            scope.launch { previewerState.openTransform(index) }
                        }
                    }
                )
            }
        }

        else -> {
            VerticalGrid(
                columns = 3,
                itemCount = files.count(),
                contentPadding = PaddingValues(2.dp),
                modifier = modifier,
            ) { index ->
                TransformImageView(
                    key = gridId + files[index].id,
                    painter = rememberAsyncImagePainter(model = files[index].thumbnailUrl),
                    previewerState = previewerState,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures {
                            scope.launch { previewerState.openTransform(index) }
                        }
                    }
                )
            }
        }
    }

    if (previewerState.visibleTarget == true || previewerState.visible) {
        Popup(
            onDismissRequest = { scope.launch { previewerState.close() } },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
            ),
        ) {
            BoxWithConstraints(modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)) {
                ImagePreviewer(
                    count = files.count(),
                    state = previewerState,
                    imageLoader = { index ->
                        val request = ImageRequest.Builder(LocalContext.current)
                            .data(files[index].url)
                            .size(coil.size.Size.ORIGINAL)
                            .build()
                        rememberAsyncImagePainter(request)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}