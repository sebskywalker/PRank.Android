package com.sebs.fitnessapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.databinding.FragmentUserLegendBinding
import com.sebs.fitnessapp.ui.LoginActivity
import com.sebs.fitnessapp.ui.MainActivity

class UserLegendFragment : Fragment() {

    private var _binding: FragmentUserLegendBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLegendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("LegendPrefs", 0)

        cargarDatos(sharedPreferences)

        binding.btnEditar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun cargarDatos(sharedPreferences: android.content.SharedPreferences) {
        binding.tvLegendName.text = sharedPreferences.getString("legendName", "N/A")
        binding.tvLegendAlias.text = sharedPreferences.getString("legendAlias", "N/A")
        binding.tvLegendDescription.text = sharedPreferences.getString("legendDescription", "N/A")
        binding.tvLegendBirthdate.text = sharedPreferences.getString("legendBirthdate", "N/A")
        binding.tvLegendOccupation.text = sharedPreferences.getString("legendOccupation", "N/A")
        val imageUriString = sharedPreferences.getString("legendImageUri", null)
        imageUriString?.let {
            Glide.with(this).load(Uri.parse(it)).into(binding.ivLegendImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}