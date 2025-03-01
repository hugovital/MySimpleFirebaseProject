package com.hugo.mysimplefirebaseproject.presentation

import android.os.Trace
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hugo.mysimplefirebaseproject.domain.repository.CatFactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.hugo.mysimplefirebaseproject.data.api.CatFactApi

class MainViewModel(private val repository: CatFactRepository) : ViewModel() {
    private val _catFact = MutableStateFlow<String?>(null)
    val catFact: StateFlow<String?> = _catFact

    fun fetchCatFact() {

        Trace.beginSection( "CAWABANGA_EVENT")

        Thread.sleep(10000)

        Trace.endSection()

        viewModelScope.launch {
            repository.getCatFact()
                .onSuccess { _catFact.value = it.fact }
                .onFailure { _catFact.value = "Error: ${it.message}" }
        }
    }
}

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(CatFactRepository(CatFactApi())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 