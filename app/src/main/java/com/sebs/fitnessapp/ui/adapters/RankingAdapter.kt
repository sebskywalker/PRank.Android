package com.sebs.fitnessapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.data.remote.model.RankingDto
import com.sebs.fitnessapp.databinding.ItemRankingBinding

class RankingAdapter(private var rankings: List<RankingDto>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    inner class RankingViewHolder(val binding: ItemRankingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val ranking = rankings[position]

        // 🏷️ Configurar nombre y score
        holder.binding.tvName.text = ranking.name
        holder.binding.tvScore.text = "${ranking.score} kg"

        // 🖼️ Cargar imagen con Glide optimizado
        Glide.with(holder.binding.ivProfile.context)
            .load(ranking.imageUrl)
            .placeholder(R.drawable.placeholder_image) // Imagen temporal mientras carga
            .error(R.drawable.error_image)            // Imagen de error si falla la carga
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cacheo eficiente
            .circleCrop()                             // Imagen circular
            .into(holder.binding.ivProfile)
    }

    override fun getItemCount(): Int = rankings.size

    // 🔄 Método para actualizar los datos del adapter
    fun updateRankings(newRankings: List<RankingDto>) {
        rankings = newRankings
        notifyDataSetChanged()
    }
}