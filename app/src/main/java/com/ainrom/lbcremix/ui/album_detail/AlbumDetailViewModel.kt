package com.ainrom.lbcremix.ui.album_detail

import androidx.lifecycle.*
import com.ainrom.lbcremix.data.Repository
import com.ainrom.lbcremix.model.Album
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlbumDetailUiState(
    val isLoading: Boolean = true,
    val album: Album? = null,
)

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val albumId: Long? = savedStateHandle.get("albumId")

    private val _state = MutableStateFlow(AlbumDetailUiState())
    val state: StateFlow<AlbumDetailUiState> = _state

    init {
        openAlbum(albumId)
    }

    private fun openAlbum(albumId: Long?) {
        if (albumId != null) {
            viewModelScope.launch {
                val result = repository.getAlbum(albumId)
                _state.update { uiState ->
                    uiState.copy(
                        album = result,
                        isLoading = false
                    )
                }
            }
        }
    }
}
