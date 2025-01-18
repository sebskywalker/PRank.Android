package com.sebs.fitnessapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.adapters.SimpleImageAdapter

class WomenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_women, container, false)

        // Configurar RecyclerViews con datos hardcoded
        setupRecyclerView(
            view.findViewById(R.id.rvLeyenda),
            listOf(R.drawable.cory_everson)
        )

        setupRecyclerView(
            view.findViewById(R.id.rvTopGlobal),
            listOf(R.drawable.francielle)
        )

        setupRecyclerView(
            view.findViewById(R.id.rvProfesional),
            listOf(R.drawable.annabel)
        )

        return view
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<Int>) {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.adapter = SimpleImageAdapter(items)
    }
}