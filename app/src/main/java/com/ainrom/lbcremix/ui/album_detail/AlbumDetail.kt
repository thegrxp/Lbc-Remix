package com.ainrom.lbcremix.ui.album_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ainrom.lbcremix.R
import com.ainrom.lbcremix.model.Album
import com.ainrom.lbcremix.ui.components.LoadingBox
import com.ainrom.lbcremix.ui.theme.colorWhite

@Composable
fun AlbumDetail(
    navController: NavController,
    albumDetailViewModel: AlbumDetailViewModel = viewModel(),
) {
    val uiState by albumDetailViewModel.state.collectAsState()

    AlbumDetailContent(uiState = uiState, onBackPress = navController::popBackStack)
}

@Composable
fun AlbumDetailContent(
    uiState: AlbumDetailUiState,
    onBackPress: () -> Unit
) {
    if (uiState.isLoading) {
        LoadingBox()
    } else {
        uiState.album?.let {
            AlbumHeader(album = it, onBackPress = onBackPress)
        }
    }
}

@Composable
fun AlbumHeader(
    album: Album,
    onBackPress: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = rememberImagePainter(album.cover, builder = {
                crossfade(true)
            }),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.aspectRatio(1f).fillMaxWidth().align(Alignment.TopCenter),
        )
        IconButton(
            onClick = onBackPress, modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back_content_description)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = album.title,
                style = MaterialTheme.typography.h4
            )
            Text(
                text = "${stringResource(id = R.string.album_title)} ${album.id}",
                style = MaterialTheme.typography.h5.copy(color = colorWhite)
            )
        }
    }
}