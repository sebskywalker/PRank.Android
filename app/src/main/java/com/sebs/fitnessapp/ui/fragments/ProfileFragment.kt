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
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("LegendPrefs", Activity.MODE_PRIVATE)

        cargarDatos(sharedPreferences)

        binding.btnSeleccionarFoto.setOnClickListener { openGallery() }

        binding.btnGuardar.setOnClickListener {
            guardarDatos(sharedPreferences)
            // 🔥 Redirigir automáticamente a UserLegendFragment después de guardar
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserLegendFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun cargarDatos(sharedPreferences: android.content.SharedPreferences) {
        binding.etNombre.setText(sharedPreferences.getString("legendName", ""))
        binding.etApodo.setText(sharedPreferences.getString("legendAlias", ""))
        binding.etDescripcion.setText(sharedPreferences.getString("legendDescription", ""))
        binding.etFechaNacimiento.setText(sharedPreferences.getString("legendBirthdate", ""))
        binding.etOcupacion.setText(sharedPreferences.getString("legendOccupation", ""))
        binding.etPRBenchPress.setText(sharedPreferences.getString("legendBenchPress", ""))
        binding.etPRSquat.setText(sharedPreferences.getString("legendSquat", ""))
        binding.etPRDeadlift.setText(sharedPreferences.getString("legendDeadlift", ""))

        sharedPreferences.getString("legendImageUri", null)?.let {
            selectedImageUri = Uri.parse(it)
            Glide.with(this).load(selectedImageUri).into(binding.imagePreview)
        }
    }

    private fun guardarDatos(sharedPreferences: android.content.SharedPreferences) {
        val nombreNuevo = binding.etNombre.text.toString().trim()
        val apodoNuevo = binding.etApodo.text.toString().trim()
        val descripcionNueva = binding.etDescripcion.text.toString().trim()
        val fechaNacimientoNueva = binding.etFechaNacimiento.text.toString().trim()
        val ocupacionNueva = binding.etOcupacion.text.toString().trim()
        val prBenchPressNuevo = binding.etPRBenchPress.text.toString().trim()
        val prSquatNuevo = binding.etPRSquat.text.toString().trim()
        val prDeadliftNuevo = binding.etPRDeadlift.text.toString().trim()

        if (nombreNuevo.isEmpty() || apodoNuevo.isEmpty() || descripcionNueva.isEmpty()
            || fechaNacimientoNueva.isEmpty() || ocupacionNueva.isEmpty()
            || prBenchPressNuevo.isEmpty() || prSquatNuevo.isEmpty() || prDeadliftNuevo.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Por favor selecciona una foto", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPreferences.edit()
            .putString("legendName", nombreNuevo)
            .putString("legendAlias", apodoNuevo)
            .putString("legendDescription", descripcionNueva)
            .putString("legendBirthdate", fechaNacimientoNueva)
            .putString("legendOccupation", ocupacionNueva)
            .putString("legendBenchPress", prBenchPressNuevo)
            .putString("legendSquat", prSquatNuevo)
            .putString("legendDeadlift", prDeadliftNuevo)
            .putString("legendImageUri", selectedImageUri.toString())
            .apply()

        Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 100)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
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
                Toast.makeText(requireContext(), "Foto seleccionada con éxito", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "No se seleccionó ninguna foto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}