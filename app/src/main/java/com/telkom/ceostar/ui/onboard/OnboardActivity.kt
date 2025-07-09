package com.telkom.ceostar.ui.onboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.telkom.ceostar.R
import com.telkom.ceostar.ui.auth.LoginActivity
import com.telkom.ceostar.ui.home.HomeActivity

class OnboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboard)
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
        }, {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        })

    }
}