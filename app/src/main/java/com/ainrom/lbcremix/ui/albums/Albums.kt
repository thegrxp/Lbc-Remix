package com.ainrom.lbcremix.ui.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ainrom.lbcremix.R
import com.ainrom.lbcremix.model.Album
import com.ainrom.lbcremix.ui.Screen
import com.ainrom.lbcremix.ui.components.LoadingBox
import com.ainrom.lbcremix.ui.components.LoadingContent
import com.ainrom.lbcremix.ui.theme.LbcRemixShapes
import com.ainrom.lbcremix.ui.theme.colorGrey
import com.ainrom.lbcremix.ui.theme.colorWhite

@Composable
fun Albums(
    navController: NavController,
    albumsViewModel: AlbumsViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by albumsViewModel.state.collectAsState()

    AlbumsContent(
        navController = navController,
        scaffoldState = scaffoldState,
        uiState = uiState,
        onFilterInputChanged = { albumsViewModel.filterSelected(it) },
        onErrorDismiss = { albumsViewModel.errorShown(it) },
        onRefreshAlbums = { albumsViewModel.refresh() },
        onDeleteAlbum = { albumsViewModel.deleteAlbum(it) },
    )
}

@Composable
fun AlbumsContent(
    navController: NavController,
    scaffoldState: ScaffoldState,
    uiState: AlbumsUiState,
    onFilterInputChanged: (String) -> Unit,
    onErrorDismiss: (String) -> Unit,
    onRefreshAlbums: () -> Unit,
    onDeleteAlbum: (Long) -> Unit,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBarContent(onFilterInputChanged = onFilterInputChanged) }
    ) {
        LoadingContent(
            loadingContent = { LoadingBox() },
            loading = uiState.isLoading,
            onRefresh = onRefreshAlbums,
            content = {
                val onErrorDismissState by rememberUpdatedState(onErrorDismiss)
                if (uiState.errorMessage != null)
                    LaunchedEffect(uiState.errorMessage, scaffoldState) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            uiState.errorMessage,
                            duration = SnackbarDuration.Long
                        )
                        onErrorDismissState(uiState.errorMessage)
                    }

                AlbumsList(
                    albumsList = uiState.albums,
                    navController = navController,
                    onDeleteAlbum = onDeleteAlbum
                )
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopBarContent(onFilterInputChanged: (String) -> Unit) {
    var value by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant
                    )
                )
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.albums_title),
            color = colorWhite,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        TextField(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .testTag(stringResource(id = R.string.instrumented_text_field)),
            colors = TextFieldDefaults.textFieldColors(
                textColor = colorWhite,
                cursorColor = colorWhite,
                placeholderColor = colorGrey,
                focusedLabelColor = colorGrey,
                unfocusedLabelColor = colorWhite,
                disabledPlaceholderColor = colorWhite
            ),
            value = value,
            onValueChange = {
                value = it
                onFilterInputChanged(value)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_hint),
                    style = MaterialTheme.typography.body2
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
        )
    }
}

@Composable
fun AlbumsList(
    albumsList: List<Album>,
    navController: NavController,
    onDeleteAlbum: (Long) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.testTag(stringResource(id = R.string.instrumented_column)),
    ) {
        items(albumsList.size) { index ->
            AlbumItem(
                album = albumsList[index],
                navController = navController,
                onDeleteAlbum = onDeleteAlbum
            )
        }
    }
}

@Composable
fun AlbumItem(
    album: Album,
    navController: NavController,
    onDeleteAlbum: (Long) -> Unit
) {
    Card(elevation = 2.dp, shape = LbcRemixShapes.large, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        navController.navigate(Screen.AlbumDetail.createRoute("${album.id}"))
                    })
        ) {
            Image(
                painter = rememberImagePainter(album.thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = album.category.toString(),
                    style = MaterialTheme.typography.body2.copy(color = colorGrey)
                )
            }
            IconButton(
                onClick = { onDeleteAlbum(album.id) }
            ) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = null, tint = colorGrey)
            }
        }
    }
}