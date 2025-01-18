package com.sebs.fitnessapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.LegendElementBinding

class LegendAdapter(
    private val legends: MutableList<LegendDto>,
    private val onLegendClick: (LegendDto) -> Unit,
    private val onUserLegendClick: () -> Unit
) : RecyclerView.Adapter<LegendAdapter.LegendViewHolder>() {

    inner class LegendViewHolder(private val binding: LegendElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(legend: LegendDto) {
            binding.tvTitle.text = legend.title ?: "Sin título"

            Glide.with(binding.ivThumbnail.context)
                .load(legend.thumbnail ?: "")
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener {
                if (legend.isUserCreated || legend.id == "user_legend") {
                    onUserLegendClick()
                } else {
                    onLegendClick(legend)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendViewHolder {
        val binding = LegendElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LegendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LegendViewHolder, position: Int) {
        holder.bind(legends[position])
    }

    override fun getItemCount(): Int = legends.size

    fun updateLegends(newLegends: List<LegendDto>) {
        legends.clear()
        legends.addAll(newLegends)
        notifyDataSetChanged()
    }
}