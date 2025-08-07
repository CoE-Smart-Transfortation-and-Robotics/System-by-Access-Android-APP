package com.telkom.ceostar.ui.onboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ActivityOnboardBinding
import com.telkom.ceostar.ui.auth.LoginActivity

class OnboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val items = listOf(
            OnboardingItem(R.drawable.onboardone, "Pesan tiket dimana saja!", "Dengan aplikasi ini, kamu bisa pesan tiket kereta api dengan mudah dan cepat. Cukup beberapa klik, tiket sudah ada di tanganmu."),
            OnboardingItem(R.drawable.onboardone, "Pesan tiket dimana saja!", "Dengan aplikasi ini, kamu bisa pesan tiket kereta api dengan mudah dan cepat. Cukup beberapa klik, tiket sudah ada di tanganmu."),
            OnboardingItem(R.drawable.onboardone, "Pesan tiket dimana saja!", "Dengan aplikasi ini, kamu bisa pesan tiket kereta api dengan mudah dan cepat. Cukup beberapa klik, tiket sudah ada di tanganmu."),
            OnboardingItem(R.drawable.onboardone, "Pesan tiket dimana saja!", "Dengan aplikasi ini, kamu bisa pesan tiket kereta api dengan mudah dan cepat. Cukup beberapa klik, tiket sudah ada di tanganmu."),
        )

        viewPager.adapter = OnboardingAdapter(items, {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

    }
}