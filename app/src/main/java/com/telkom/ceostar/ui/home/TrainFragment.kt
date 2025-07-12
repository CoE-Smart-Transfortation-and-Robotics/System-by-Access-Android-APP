package com.telkom.ceostar.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.FragmentTrainBinding
import com.telkom.ceostar.databinding.FragmentUserBinding
import com.telkom.ceostar.ui.recylerview.MenuUser
import com.telkom.ceostar.ui.recylerview.MenuUserAdapter
import com.telkom.ceostar.ui.recylerview.TrainType
import com.telkom.ceostar.ui.recylerview.TrainTypeAdapter
import com.telkom.ceostar.ui.train.TrainTypeActivity
import com.telkom.ceostar.ui.user.ProfileActivity

class TrainFragment : Fragment() {

    private var _binding: FragmentTrainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTrainBinding.bind(view)

        val trainType = listOf(
            TrainType(
                title = "Antar Kota",
                trainImage = R.drawable.antar_kota,
                onClick = {
                    val goToType = Intent(context, TrainTypeActivity::class.java)
                    startActivity(goToType)
                }
            ),
            TrainType(
                title = "LRT",
                trainImage = R.drawable.lrt,
                onClick = {
                }
            ),
            TrainType(
                title = "Lokal",
                trainImage = R.drawable.lokal,
                onClick = {
                }
            ),
            TrainType(
                title = "Commuter Line",
                trainImage = R.drawable.commuter,
                onClick = {
                }
            ),
            TrainType(
                title = "Whoosh",
                trainImage = R.drawable.whoosh,
                onClick = {
                }
            )
        )

        val trainTypeAdapter = TrainTypeAdapter(trainType)
        binding.rvTrainList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trainTypeAdapter
        }

    }
}