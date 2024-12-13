package com.sebs.fitnessapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sebs.fitnessapp.R

class CarouselAdapter(
    private val imageResources: List<Int> // Lista de imágenes del carrusel desde drawable
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    // ViewHolder para cada elemento del carrusel
    inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.carouselImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel_image, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val imageResId = imageResources[position]
        Glide.with(holder.itemView.context)
            .load(imageResId) // Carga la imagen desde drawable
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = imageResources.size
}