package com.sebs.fitnessapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.databinding.FragmentUserLegendBinding
import com.sebs.fitnessapp.ui.LoginActivity

class UserLegendFragment : Fragment() {

    private var _binding: FragmentUserLegendBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLegendBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            redirectToLogin()
            return
        }

        val sharedPreferences = requireContext().getSharedPreferences("LegendPrefs", 0)
        cargarDatos(sharedPreferences)

        binding.btnEditar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            redirectToLogin()
        }
    }

    private fun cargarDatos(sharedPreferences: android.content.SharedPreferences) {
        binding.tvLegendName.text = sharedPreferences.getString("legendName", "Nuevo Usuario")
        binding.tvLegendAlias.text = sharedPreferences.getString("legendAlias", "Alias")
        binding.tvLegendDescription.text = sharedPreferences.getString("legendDescription", "Descripción pendiente")

        // ✅ PRs ahora se cargan correctamente
        binding.tvLegendBenchPressPR.text = "Bench Press PR: ${sharedPreferences.getString("legendBenchPress", "0")} kg"
        binding.tvLegendSquatPR.text = "Squat PR: ${sharedPreferences.getString("legendSquat", "0")} kg"
        binding.tvLegendDeadliftPR.text = "Deadlift PR: ${sharedPreferences.getString("legendDeadlift", "0")} kg"

        sharedPreferences.getString("legendImageUri", null)?.let {
            Glide.with(this).load(Uri.parse(it)).into(binding.ivLegendImage)
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}