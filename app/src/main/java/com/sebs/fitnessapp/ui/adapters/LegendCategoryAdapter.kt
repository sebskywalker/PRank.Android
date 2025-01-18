package com.sebs.fitnessapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sebs.fitnessapp.data.remote.model.LegendCategory
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.ItemCategoryBinding
import com.sebs.fitnessapp.ui.fragments.LegendDetailFragment
import com.sebs.fitnessapp.ui.fragments.UserLegendFragment

class LegendCategoryAdapter(
    private var categories: MutableList<LegendCategory>,
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

        val legendAdapter = LegendAdapter(category.legends.toMutableList(), { legend ->
            val activity = holder.itemView.context as? FragmentActivity
            navigateToLegendDetail(activity, legend)
        }, {
            val activity = holder.itemView.context as? FragmentActivity
            activity?.let {
                Toast.makeText(it, "Leyenda de usuario seleccionada", Toast.LENGTH_SHORT).show()
                it.supportFragmentManager.beginTransaction()
                    .replace(com.sebs.fitnessapp.R.id.fragment_container, UserLegendFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })

        holder.binding.legendRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
            adapter = legendAdapter
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<LegendCategory>) {
        this.categories.clear()
        this.categories.addAll(newCategories)
        notifyDataSetChanged()
    }

    private fun navigateToLegendDetail(activity: FragmentActivity?, legend: LegendDto) {
        activity?.let {
            when {
                legend.isUserCreated || legend.id == "user_legend" -> {
                    it.supportFragmentManager.beginTransaction()
                        .replace(com.sebs.fitnessapp.R.id.fragment_container, UserLegendFragment())
                        .addToBackStack(null)
                        .commit()
                }
                !legend.id.isNullOrEmpty() -> {
                    it.supportFragmentManager.beginTransaction()
                        .replace(com.sebs.fitnessapp.R.id.fragment_container, LegendDetailFragment.newInstance(legend.id!!))
                        .addToBackStack(null)
                        .commit()
                }
                else -> {
                    Toast.makeText(it, "Leyenda no disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}