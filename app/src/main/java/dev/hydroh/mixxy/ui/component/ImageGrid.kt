package dev.hydroh.mixxy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.origeek.imageViewer.previewer.ImagePreviewer
import com.origeek.imageViewer.previewer.TransformImageView
import com.origeek.imageViewer.previewer.VerticalDragType
import com.origeek.imageViewer.previewer.rememberPreviewerState
import dev.hydroh.mixxy.data.remote.model.DriveFile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ImageGrid(
    files: ImmutableList<DriveFile>,
    modifier: Modifier = Modifier,
) {
    val gridId = remember { UUID.randomUUID().toString() }
    val previewerState =
        rememberPreviewerState(
            verticalDragType = VerticalDragType.UpAndDown,
            pageCount = { files.count() }
        ) { index ->
            gridId + index.toString()
        }
    val scope = rememberCoroutineScope()
    val contentPadding = 4.dp
    val roundCorner = 6.dp

    when (files.count()) {
        1 -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
            ) {
                TransformImageView(
                    key = gridId + "0",
                    painter = rememberAsyncImagePainter(model = files[0].thumbnailUrl),
                    previewerState = previewerState,
                    modifier = Modifier
                        .clickable {
                            scope.launch { previewerState.openTransform(0) }
                        }
                        .clip(RoundedCornerShape(roundCorner))
                )
            }
        }

        3 -> {
            Row(modifier = modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .aspectRatio(0.75f),
                ) {
                    TransformImageView(
                        key = gridId + "0",
                        painter = rememberAsyncImagePainter(model = files[0].thumbnailUrl),
                        previewerState = previewerState,
                        modifier = Modifier
                            .clickable {
                                scope.launch { previewerState.openTransform(0) }
                            }
                            .clip(RoundedCornerShape(4.dp))
                            .fillMaxSize()
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(contentPadding)
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.75f)
                        .weight(1f),
                ) {
                    TransformImageView(
                        key = gridId + "1",
                        painter = rememberAsyncImagePainter(model = files[1].thumbnailUrl),
                        previewerState = previewerState,
                        modifier = Modifier
                            .clickable {
                                scope.launch { previewerState.openTransform(1) }
                            }
                            .clip(RoundedCornerShape(roundCorner))
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    Spacer(
                        modifier = Modifier
                            .height(contentPadding)
                            .fillMaxWidth()
                    )
                    TransformImageView(
                        key = gridId + "2",
                        painter = rememberAsyncImagePainter(model = files[2].thumbnailUrl),
                        previewerState = previewerState,
                        modifier = Modifier
                            .clickable {
                                scope.launch { previewerState.openTransform(2) }
                            }
                            .clip(RoundedCornerShape(roundCorner))
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }
        }

        else -> {
            VerticalGrid(
                columns = 2,
                itemCount = files.count(),
                spacing = contentPadding,
                modifier = modifier.fillMaxSize(),
                aspectRatio = 1f,
            ) { index ->
                TransformImageView(
                    key = gridId + index.toString(),
                    painter = rememberAsyncImagePainter(model = files[index].thumbnailUrl),
                    previewerState = previewerState,
                    modifier = Modifier
                        .clickable {
                            scope.launch { previewerState.openTransform(0) }
                        }
                        .clip(RoundedCornerShape(roundCorner))
                )
            }
        }
    }

    if (previewerState.visibleTarget == true || previewerState.visible) {
        Popup(
            onDismissRequest = { scope.launch { previewerState.close() } },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                ImagePreviewer(
                    state = previewerState,
                    imageLoader = { index ->
                        val request = ImageRequest.Builder(LocalContext.current)
                            .data(files[index].url)
                            .size(coil.size.Size.ORIGINAL)
                            .build()
                        rememberAsyncImagePainter(request)
                    },
                    detectGesture = {
                        onTap = { scope.launch { previewerState.close() } }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
