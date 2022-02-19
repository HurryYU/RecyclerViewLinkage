package com.hurryyu.tworecyclerviewlinkage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hurryyu.tworecyclerviewlinkage.state.MainUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = MainRepository()

    private val _mainUiStateFlow = MutableStateFlow(MainUiState())
    val mainStateFlow: StateFlow<MainUiState> = _mainUiStateFlow

    private val _selectedGroupNameChangeFlow = MutableSharedFlow<String>()
    val productListScrollPositionFlow = _selectedGroupNameChangeFlow.mapLatest {
        repository.findFirstGroupPositionByGroupName(it)
    }

    private var job: Job? = null

    init {
        loadProductsData()
    }

    fun changeClassifySelectedIndex(newIndex: Int) {
        _mainUiStateFlow.update {
            it.copy(currentSelectedIndex = newIndex)
        }
    }

    fun changeClassifySelectedIndexByGroupName(groupName: String) {
        job?.cancel()
        job = viewModelScope.launch {
            _mainUiStateFlow.update {
                it.copy(currentSelectedIndex = repository.findClassifyIndexByGroupName(groupName))
            }
        }
    }

    fun adjustProductListPosition(groupName: String) {
        viewModelScope.launch {
            _selectedGroupNameChangeFlow.emit(groupName)
        }
    }

    private fun loadProductsData() {
        viewModelScope.launch {
            _mainUiStateFlow.update {
                it.copy(isLoading = false, productsData = repository.loadProducts())
            }
        }
    }

}