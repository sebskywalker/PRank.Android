package com.sebs.fitnessapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.LegendElementBinding

class LegendAdapter(
    private val legends: MutableList<LegendDto>,
    private val onLegendClick: (LegendDto) -> Unit
) : RecyclerView.Adapter<LegendAdapter.LegendViewHolder>() {

    inner class LegendViewHolder(val binding: LegendElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(legend: LegendDto) {
            binding.tvTitle.text = legend.title ?: "Sin título"
            binding.tvOccupation.text = legend.occupation ?: "Sin ocupación"
            binding.tvPRBenchPress.text = "PR: ${legend.prBenchPress ?: "N/A"}"

            // Log para verificar la URL de la imagen
            Log.d("LegendAdapter", "Cargando imagen: ${legend.thumbnail}")

            // Usar Glide para cargar la imagen desde la URL
            Glide.with(binding.ivThumbnail.context)
                .load(legend.thumbnail)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener {
                onLegendClick(legend)
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
}