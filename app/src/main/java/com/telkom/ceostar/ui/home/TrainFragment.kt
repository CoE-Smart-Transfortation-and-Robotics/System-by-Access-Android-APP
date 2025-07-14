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
                    goToType.putExtra("EXTRA_TYPE", "Antar Kota")
                    startActivity(goToType)
                }
            ),
            TrainType(
                title = "LRT",
                trainImage = R.drawable.lrt,
                onClick = {
                    val goToType = Intent(context, TrainTypeActivity::class.java)
                    goToType.putExtra("EXTRA_TYPE", "LRT")
                    startActivity(goToType)
                }
            ),
            TrainType(
                title = "Lokal",
                trainImage = R.drawable.lokal,
                onClick = {
                    val goToType = Intent(context, TrainTypeActivity::class.java)
                    goToType.putExtra("EXTRA_TYPE", "Lokal")
                    startActivity(goToType)
                }
            ),
            TrainType(
                title = "Commuter Line",
                trainImage = R.drawable.commuter,
                onClick = {
                    val goToType = Intent(context, TrainTypeActivity::class.java)
                    goToType.putExtra("EXTRA_TYPE", "Commuter Line")
                    startActivity(goToType)
                }
            ),
            TrainType(
                title = "Whoosh",
                trainImage = R.drawable.whoosh,
                onClick = {
                    val goToType = Intent(context, TrainTypeActivity::class.java)
                    goToType.putExtra("EXTRA_TYPE", "Whoosh")
                    startActivity(goToType)
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