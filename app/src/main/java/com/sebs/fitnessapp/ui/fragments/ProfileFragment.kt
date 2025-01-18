package com.sebs.fitnessapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = requireContext().getSharedPreferences("LegendPrefs", Activity.MODE_PRIVATE)
        cargarDatos(sharedPreferences)

        binding.btnSeleccionarFoto.setOnClickListener { openGallery() }

        binding.btnGuardar.setOnClickListener {
            guardarDatos(sharedPreferences)
            Toast.makeText(requireContext(), "Leyenda guardada correctamente", Toast.LENGTH_SHORT).show()

            // 🔄 Redirige a UserLegendFragment para reflejar los cambios
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserLegendFragment())
                .commit()
        }
    }

    private fun cargarDatos(sharedPreferences: android.content.SharedPreferences) {
        binding.etNombre.setText(sharedPreferences.getString("legendName", ""))
        binding.etApodo.setText(sharedPreferences.getString("legendAlias", ""))
        binding.etDescripcion.setText(sharedPreferences.getString("legendDescription", ""))
        binding.etPRBenchPress.setText(sharedPreferences.getString("legendBenchPress", "0"))
        binding.etPRSquat.setText(sharedPreferences.getString("legendSquat", "0"))
        binding.etPRDeadlift.setText(sharedPreferences.getString("legendDeadlift", "0"))

        sharedPreferences.getString("legendImageUri", null)?.let {
            selectedImageUri = Uri.parse(it)
            Glide.with(this).load(selectedImageUri).into(binding.imagePreview)
        }
    }

    private fun guardarDatos(sharedPreferences: android.content.SharedPreferences) {
        val nombreNuevo = binding.etNombre.text.toString().trim()
        val apodoNuevo = binding.etApodo.text.toString().trim()
        val descripcionNueva = binding.etDescripcion.text.toString().trim()
        val benchPress = binding.etPRBenchPress.text.toString().trim()
        val squat = binding.etPRSquat.text.toString().trim()
        val deadlift = binding.etPRDeadlift.text.toString().trim()

        sharedPreferences.edit()
            .putString("legendName", nombreNuevo)
            .putString("legendAlias", apodoNuevo)
            .putString("legendDescription", descripcionNueva)
            .putString("legendBenchPress", benchPress)
            .putString("legendSquat", squat)
            .putString("legendDeadlift", deadlift)
            .putString("legendImageUri", selectedImageUri.toString())
            .apply()
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 100)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let {
                Glide.with(this).load(it).into(binding.imagePreview)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}