package com.telkom.ceostar.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.FragmentHomeBinding
import com.telkom.ceostar.ui.recylerview.TrainType
import com.telkom.ceostar.ui.recylerview.TrainTypeAdapter
import com.telkom.ceostar.ui.train.TrainTypeActivity
import com.telkom.core.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)

        val trainType = listOf(
            TrainType(
                title = "Antar Kota",
                trainImage = R.drawable.antar_kota,
//                trainImage = R.drawable.train_icon,
                onClick = {
                    goToTrainType("Antar Kota", 1)
                }
            ),
            TrainType(
                title = "LRT",
                trainImage = R.drawable.lrt,
//                trainImage = R.drawable.train_icon,
                onClick = {
                    goToTrainType("Antar Kota", 1)
                }
            ),
            TrainType(
                title = "Lokal",
                trainImage = R.drawable.lokal,
//                trainImage = R.drawable.train_icon,
                onClick = {
                    goToTrainType("Antar Kota", 1)
                }
            ),
            TrainType(
                title = "Commuter Line",
                trainImage = R.drawable.commuter,
//                trainImage = R.drawable.train_icon,
                onClick = {
                    goToTrainType("Antar Kota", 1)
                }
            ),
            TrainType(
                title = "Whoosh",
                trainImage = R.drawable.whoosh,
//                trainImage = R.drawable.train_icon,
                onClick = {
                    goToTrainType("Antar Kota", 1)
                }
            )
        )

        val trainTypeAdapter = TrainTypeAdapter(trainType)
        binding.rvTrainList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trainTypeAdapter
        }

        observeProfile()
        viewModel.getProfile()
    }

    private fun goToTrainType(type: String, typeId: Int) {
        val goToType = Intent(context, TrainTypeActivity::class.java)
        goToType.putExtra("EXTRA_TYPE", type)
        goToType.putExtra("EXTRA_TYPE_ID", typeId)
        startActivity(goToType)

    }

    private fun observeProfile(){

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            android.util.Log.d("UserFragment", "Profile received: $profile")
            binding.namaUser.text = profile.name

        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            android.util.Log.d("UserFragment", "Loading state: $isLoading")
            if (!isLoading) {
                binding.namaUser.visibility = View.VISIBLE
            }
        }
    }

}