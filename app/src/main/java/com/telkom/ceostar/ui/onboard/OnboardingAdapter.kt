package com.telkom.ceostar.ui.onboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telkom.ceostar.R

class OnboardingAdapter(private val items: List<OnboardingItem>, private val onButtonClick: () -> Unit, private val onSecondaryClick: () -> Unit) : RecyclerView.Adapter<OnboardingAdapter.OnBoardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false)
        return OnBoardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageRes)
        holder.titleText.text = item.text
        holder.descriptionText.text = item.descText

        if (position == items.size - 1) {
            holder.btnNext.visibility = View.VISIBLE
            holder.btnNext.setOnClickListener {
                onButtonClick()
            }
            holder.btnSkip.visibility = View.VISIBLE
            holder.btnSkip.setOnClickListener {
                onSecondaryClick
            }
        } else {
            holder.btnNext.visibility = View.GONE
            holder.btnSkip.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size


    class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val descriptionText: TextView = itemView.findViewById(R.id.detailText)
        val btnNext: View = itemView.findViewById(R.id.btnNext)
        val btnSkip: View = itemView.findViewById(R.id.btnSkip)
    }
}