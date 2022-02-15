package com.ainrom.lbcremix.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainrom.lbcremix.data.Repository
import com.ainrom.lbcremix.model.Album
import com.ainrom.lbcremix.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class AlbumsUiState(
    val isLoading: Boolean = true,
    val albums: List<Album> = emptyList(),
    val errorMessage: String? = null,
)

data class FilterState(
    val id: Int = 0,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumsUiState())
    val state: StateFlow<AlbumsUiState> = _state

    private val _filter = MutableStateFlow(FilterState())

    init {
        viewModelScope.launch {
            _filter.flatMapLatest { filter ->
                if (filter.id == 0)
                    repository.albums()
                else
                    repository.albumsByCategory(filter.id)
            }.collect { result ->
                _state.update { uiState ->
                    when (result) {
                        is Resource.Success -> uiState.copy(
                            albums = result.data ?: emptyList(),
                            isLoading = false
                        )
                        is Resource.Error -> uiState.copy(
                            errorMessage = result.error?.message,
                            isLoading = false
                        )
                        is Resource.Loading -> uiState.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun filterSelected(filter: String) {
        val value = filter.toIntOrNull()
        if (value != null && value != 0) {
            _filter.update { it.copy(id = value) }
        } else {
            _filter.update { it.copy(id = 0) }
        }
    }

    fun refresh() {
        _filter.update { filter -> filter.copy(id = 0) }

        viewModelScope.launch {
            val result = repository.refresh()
            _state.update { uiState ->
                when (result) {
                    is Resource.Success -> uiState.copy(
                        isLoading = false
                    )
                    is Resource.Error -> uiState.copy(
                        errorMessage = "Error while refreshing",
                        isLoading = false
                    )
                    is Resource.Loading -> uiState
                }
            }
        }
    }

    /**
     * Notify that an error was displayed on the screen
     */
    fun errorShown(errorMessage: String) {
        if (_state.value.errorMessage == errorMessage)
            _state.update { uiState ->
                uiState.copy(errorMessage = null)
            }
    }

    fun deleteAlbum(albumId: Long) {
        viewModelScope.launch {
            repository.deleteAlbum(albumId)
        }
    }
}