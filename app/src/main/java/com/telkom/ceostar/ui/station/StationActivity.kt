package com.telkom.ceostar.ui.station

import android.app.Activity
import android.content.Intent
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

    companion object {
        const val EXTRA_STATION_NAME = "extra_station_name"
        const val EXTRA_STATION_CODE = "extra_station_code"
        const val EXTRA_STATION_ID = "extra_station_id"
        const val EXTRA_TYPE = "extra_type"
    }

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
                    val resultIntent = Intent()

                    // 2. Masukkan data yang ingin dikirim kembali
                    resultIntent.putExtra(EXTRA_STATION_NAME, station.station_name)
                    resultIntent.putExtra(EXTRA_STATION_CODE, station.station_code)
                    resultIntent.putExtra(EXTRA_STATION_ID, station.id)
                    resultIntent.putExtra(EXTRA_TYPE, intent.getStringExtra(EXTRA_TYPE))

                    // 3. Atur hasilnya menjadi OK dan sertakan intent data
                    setResult(Activity.RESULT_OK, resultIntent)

                    // 4. Tutup activity ini
                    finish()
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