package com.telkom.ceostar.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.viewModels
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.telkom.ceostar.R
import com.telkom.ceostar.core.utils.SessionManager
import com.telkom.ceostar.databinding.FragmentUserBinding
import com.telkom.ceostar.ui.onboard.OnboardActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.core.viewmodel.UserViewModel
import com.telkom.ceostar.ui.auth.ConfirmationActivity
import com.telkom.ceostar.ui.recylerview.MenuUser
import com.telkom.ceostar.ui.recylerview.MenuUserAdapter
import com.telkom.ceostar.ui.user.ProfileActivity

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserBinding.bind(view)

        val menuList = listOf(
            MenuUser(
                title = "Ganti Kata Sandi",
                iconLeft = R.drawable.password_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {

                }
            ),
            MenuUser(
                title = "Riwayat Transaksi",
                iconLeft = R.drawable.list_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = "Benefit Member",
                iconLeft = R.drawable.member_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = "Metode Pembayaran",
                iconLeft = R.drawable.payment_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = "Registrasi Face Recognition",
                iconLeft = R.drawable.face_scan_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = "Pusat Bantuan",
                iconLeft = R.drawable.help_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = "Tentang CoEStar",
                iconLeft = R.drawable.about_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = "Ganti Bahasa",
                iconLeft = R.drawable.translation_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = "Keluar",
                iconLeft = R.drawable.logout_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                    createExitPopUp()
                }
            )

        )

        val menuUserAdapter = MenuUserAdapter(menuList)
        binding.rvButtonUser.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuUserAdapter
        }

//        binding.profileButton.setOnClickListener {
//            val goToProfile = Intent(context, ProfileActivity::class.java)
//            goToProfile.putExtra("EXTRA_NAME", )
//            startActivity(goToProfile)
//        }

        observeProfile()
        viewModel.getProfile()
    }

    private fun observeProfile(){

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            android.util.Log.d("UserFragment", "Profile received: $profile")
            binding.userName.text = profile.name
            binding.additionalInfo.text = profile.nik

            binding.profileButton.setOnClickListener {
                val goToProfile = Intent(context, ProfileActivity::class.java)
                goToProfile.putExtra("EXTRA_PROFILE", profile)
//                goToProfile.putExtra("EXTRA_NAME", profile.name)
//                goToProfile.putExtra("EXTRA_EMAIL", profile.email)
//                goToProfile.putExtra("EXTRA_PHONE", profile.phone)
//                goToProfile.putExtra("EXTRA_ADDRESS", profile.address)
//                goToProfile.putExtra("EXTRA_NIK", profile.nik)
                startActivity(goToProfile)
            }

        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            android.util.Log.d("UserFragment", "Loading state: $isLoading")
            if (!isLoading) {
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

    override fun onResume() {
        super.onResume()
        observeProfile()
        viewModel.getProfile()
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