package com.telkom.ceostar.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.viewModels
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.telkom.ceostar.R
import com.telkom.ceostar.core.utils.SessionManager
import com.telkom.ceostar.databinding.FragmentUserBinding
import com.telkom.ceostar.ui.onboard.OnboardActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.graphics.drawable.toDrawable
import com.telkom.ceostar.core.viewmodel.UserViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    lateinit var popUpSignOut: Dialog
    lateinit var cancelButton: MaterialButton
    lateinit var signOutButton: MaterialButton

    private val viewModel: UserViewModel by viewModels()

//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
//        observeProfile()
//
//        binding.loadingView.visibility = View.VISIBLE
//        binding.profileImage.setBackgroundColor(android.graphics.Color.TRANSPARENT)
//        binding.profileImage.setImageResource(0)
//
//        view.post {
//            viewModel.getProfile()
//        }
    }

    private fun observeProfile(){
        android.util.Log.d("UserFragment", "Setting up observer")

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            android.util.Log.d("UserFragment", "Profile received: $profile")
            binding.userName.text = profile.name
            binding.additionalInfo.text = profile.email
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            android.util.Log.d("UserFragment", "Loading state: $isLoading")
            if (!isLoading) {
                binding.loadingView.visibility = View.GONE
                binding.userName.visibility = View.VISIBLE
                binding.additionalInfo.visibility = View.VISIBLE
                binding.profileImage.visibility = View.VISIBLE
                binding.profileImage.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.secondary)
                )
                binding.profileImage.setImageResource(R.drawable.onboardtwo)
            }
        }
    }

    private fun setupClickListeners() {
        binding.signOutButton.setOnClickListener {
            createExitPopUp()
        }
    }

    private fun createExitPopUp() {
        popUpSignOut = Dialog(requireContext())
        popUpSignOut.setContentView(R.layout.popup_danger)
        cancelButton = popUpSignOut.findViewById(R.id.cancel_button)
        signOutButton = popUpSignOut.findViewById(R.id.sign_out_button)

        cancelButton.setOnClickListener {
            popUpSignOut.dismiss()
        }
        signOutButton.setOnClickListener {
            signOut()
        }

        popUpSignOut.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        popUpSignOut.show()
    }

    private fun signOut() {
        sessionManager.clearAuthToken()

        val intent = Intent(requireActivity(), OnboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = UserFragment()
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment UserFragment.
//         */
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            UserFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}