package com.telkom.ceostar.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.FragmentUserBinding
import com.telkom.ceostar.ui.onboard.OnboardActivity
import com.telkom.ceostar.ui.recylerview.MenuUser
import com.telkom.ceostar.ui.recylerview.MenuUserAdapter
import com.telkom.ceostar.ui.user.AboutActivity
import com.telkom.ceostar.ui.user.ProfileActivity
import com.telkom.core.utils.SessionManager
import com.telkom.core.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
                title = getString(R.string.transaction),
                iconLeft = R.drawable.list_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = getString(R.string.member_benefit),
                iconLeft = R.drawable.member_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = getString(R.string.payment_method),
                iconLeft = R.drawable.payment_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = getString(R.string.face_recognition),
                iconLeft = R.drawable.face_scan_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = getString(R.string.help_center),
                iconLeft = R.drawable.help_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                }
            ),
            MenuUser(
                title = getString(R.string.about),
                iconLeft = R.drawable.about_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                    val intent = Intent(context, AboutActivity::class.java)
                    startActivity(intent)
                }
            ),
//            MenuUser(
//                title = getString(R.string.change_language),
//                iconLeft = R.drawable.translation_icon,
//                viewType = ViewType.LANGUAGE_TOGGLE
//            ),
            MenuUser(
                title = getString(R.string.logout),
                iconLeft = R.drawable.logout_icon,
                iconRight = R.drawable.right_arrow,
                onClick = {
                    createExitPopUp()
                }
            )

        )

        val menuAdapter = MenuUserAdapter(menuList) {
            // Panggil recreate dari Activity yang menaungi fragment ini
            requireActivity().recreate()
        }

        // 2. HAPUS instance adapter kedua yang duplikat
        // val menuUserAdapter = MenuUserAdapter(menuList) <-- Hapus baris ini

        // 3. Set adapter yang benar ke RecyclerView
        binding.rvButtonUser.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter // Gunakan 'menuAdapter' yang sudah punya callback
        }


//        val menuAdapter = MenuUserAdapter(menuList) {
//            // Aksi ini akan dipanggil saat toggle bahasa diubah
//            recreate() // Method bawaan Activity untuk menggambar ulang dirinya
//        }
//
//        val menuUserAdapter = MenuUserAdapter(menuList)
//        binding.rvButtonUser.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = menuUserAdapter
//        }

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

}