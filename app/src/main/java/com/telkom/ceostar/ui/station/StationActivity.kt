package com.telkom.ceostar.ui.station

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.R
import com.telkom.ceostar.core.viewmodel.StationViewModel
import com.telkom.ceostar.databinding.ActivityStationBinding
import com.telkom.ceostar.ui.recylerview.StationList
import com.telkom.ceostar.ui.recylerview.StationListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStationBinding
    private val viewModel: StationViewModel by viewModels()
    private lateinit var stationAdapter: StationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        // Load stations data
        viewModel.loadStations()
    }

    private fun setupRecyclerView() {
        stationAdapter = StationListAdapter(emptyList())
        binding.rvStationList.layoutManager = LinearLayoutManager(this)
        binding.rvStationList.adapter = stationAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Tidak perlu aksi khusus saat submit
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Panggil fungsi search di ViewModel setiap kali teks berubah
                viewModel.searchStation(newText)
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.stations.observe(this) { stations ->
            val stationList = stations.map { station ->
                StationList(
                    stationName = station.station_name,
                    stationLocation = station.station_code
                ) {
                    Toast.makeText(this, "${station.station_name} clicked", Toast.LENGTH_SHORT).show()
                }
            }
            // Perbarui data di adapter yang sudah ada
            stationAdapter.updateData(stationList)
        }

        viewModel.loading.observe(this) { isLoading ->
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}