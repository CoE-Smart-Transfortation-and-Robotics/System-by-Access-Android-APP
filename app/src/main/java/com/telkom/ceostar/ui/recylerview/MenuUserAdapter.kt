package com.telkom.ceostar.ui.recylerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ViewButtonUserBinding

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class MenuUserAdapter(
    private val menuList: List<MenuUser>,
    private val onLanguageChange: () -> Unit // Callback untuk merefresh activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 1. Tentukan ViewHolder untuk setiap tipe
    inner class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val iconLeft: ImageView = view.findViewById(R.id.icon_left)
        val iconRight: ImageView = view.findViewById(R.id.icon_right)
    }

    inner class LanguageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val iconLeft: ImageView = view.findViewById(R.id.icon_left)
        val languageSwitch: SwitchMaterial = view.findViewById(R.id.language_switch)
    }

    // 2. Override getItemViewType
    override fun getItemViewType(position: Int): Int {
        return menuList[position].viewType
    }

    // 3. Buat ViewHolder yang sesuai di onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.LANGUAGE_TOGGLE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_language_toggle, parent, false)
            LanguageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_button_user, parent, false)
            DefaultViewHolder(view)
        }
    }

    override fun getItemCount() = menuList.size

    // 4. Bind data ke ViewHolder yang sesuai
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menu = menuList[position]

        when (holder) {
            is LanguageViewHolder -> {
                holder.title.text = menu.title
                holder.iconLeft.setImageResource(menu.iconLeft)

                // Bagian ini tetap sama, untuk mengatur tampilan awal switch
                val currentLangForDisplay = AppCompatDelegate.getApplicationLocales().toLanguageTags()
                holder.languageSwitch.isChecked = currentLangForDisplay.contains("en")

                holder.languageSwitch.setOnCheckedChangeListener(null)

                // Tambahkan listener baru dengan logika yang diperbaiki
                holder.languageSwitch.setOnCheckedChangeListener { _, _ -> // Parameter isChecked tidak kita perlukan lagi
                    // --- LOGIKA BARU (Perbaikan) ---

                    // 1. Cek dulu bahasa yang sedang aktif SAAT INI, sebelum diubah
                    val currentLangOnClick = AppCompatDelegate.getApplicationLocales().toLanguageTags()

                    // 2. Tentukan bahasa baru sebagai kebalikannya
                    // Jika sekarang Inggris, ganti ke Indonesia. Jika bukan Inggris, ganti ke Inggris.
                    val newLang = if (currentLangOnClick.contains("en")) "in" else "en"

                    // 3. Terapkan bahasa baru
                    val appLocale = LocaleListCompat.forLanguageTags(newLang)
                    AppCompatDelegate.setApplicationLocales(appLocale)

                    // 4. Panggil callback untuk refresh UI
                    onLanguageChange()
                }
            }
            is DefaultViewHolder -> {
                // Binding untuk view holder biasa
                holder.title.text = menu.title
                holder.iconLeft.setImageResource(menu.iconLeft)
                menu.iconRight?.let { holder.iconRight.setImageResource(it) }

                holder.itemView.setOnClickListener {
                    menu.onClick?.invoke()
                }
            }
        }
    }
}