package com.sebs.fitnessapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sebs.fitnessapp.data.remote.model.LegendCategory
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.ItemCategoryBinding

class LegendCategoryAdapter(
    private val categories: List<LegendCategory>,
    private val onLegendClick: (LegendDto) -> Unit
) : RecyclerView.Adapter<LegendCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.binding.categoryTitle.text = category.category
        val legendAdapter = LegendAdapter(category.legends.toMutableList(), onLegendClick)

        // Configurar el LayoutManager para el RecyclerView
        holder.binding.legendRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
            adapter = legendAdapter
        }
    }

    override fun getItemCount(): Int = categories.size
}