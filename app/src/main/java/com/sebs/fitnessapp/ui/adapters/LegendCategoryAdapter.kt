package com.sebs.fitnessapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sebs.fitnessapp.data.remote.model.LegendCategory
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.ItemCategoryBinding
import com.sebs.fitnessapp.ui.fragments.UserLegendFragment

class LegendCategoryAdapter(
    private var categories: List<LegendCategory>,
    private val onLegendClick: (LegendDto) -> Unit
) : RecyclerView.Adapter<LegendCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.binding.categoryTitle.text = category.category
        val legendAdapter = LegendAdapter(category.legends, { legend ->
            onLegendClick(legend)
        }, {
            // 🔄 Lógica para redirigir a UserLegendFragment
            val activity = holder.itemView.context as? FragmentActivity
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(com.sebs.fitnessapp.R.id.fragment_container, UserLegendFragment())
                ?.addToBackStack(null)
                ?.commit()
        })

        holder.binding.legendRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
            adapter = legendAdapter
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<LegendCategory>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }
}