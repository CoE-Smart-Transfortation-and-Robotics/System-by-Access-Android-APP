package com.telkom.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.core.data.model.Station
import com.telkom.core.data.repository.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StationViewModel @Inject constructor(private val repository: StationRepository) : ViewModel() {

    private val _stations = MutableLiveData<List<Station>>()
    val stations: LiveData<List<Station>> = _stations

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Menyimpan daftar stasiun asli yang tidak difilter
    private var originalStationList: List<Station> = listOf()

    fun loadStations() {
        viewModelScope.launch {
            _loading.value = true
            val response = repository.getAllStations()

            if (response.success) {
                originalStationList = response.data ?: emptyList()
                _stations.value = originalStationList
                _error.value = null
            } else {
                _error.value = response.message
            }
            _loading.value = false
        }
    }

    // Fungsi untuk memfilter stasiun berdasarkan query
    fun searchStation(query: String?) {
        if (query.isNullOrBlank()) {
            _stations.value = originalStationList
        } else {
            val filteredList = originalStationList.filter {
                it.station_name.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
            _stations.value = filteredList
        }
    }
}