package com.sebs.fitnessapp.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.LegendElementBinding

class LegendViewHolder(


    private val binding: LegendElementBinding

): RecyclerView.ViewHolder(binding.root) {

    fun bind(legend: LegendDto){

        binding.tvTitle.text = legend.title


        //glide

        Glide.with(binding.root.context)
            .load(legend.thumnail)
            .into(binding.ivThumbnail)

  }


}


