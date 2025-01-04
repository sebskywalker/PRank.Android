package com.sebs.fitnessapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sebs.fitnessapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar datos existentes en el formulario
        val sharedPreferences = getSharedPreferences("LegendPrefs", MODE_PRIVATE)
        val nombre = sharedPreferences.getString("legendName", "")
        val apodo = sharedPreferences.getString("legendAlias", "")
        val descripcion = sharedPreferences.getString("legendDescription", "")
        val fechaNacimiento = sharedPreferences.getString("legendBirthdate", "")
        val ocupacion = sharedPreferences.getString("legendOccupation", "")
        val prBenchPress = sharedPreferences.getString("legendBenchPress", "")
        val prSquat = sharedPreferences.getString("legendSquat", "")
        val prDeadlift = sharedPreferences.getString("legendDeadlift", "")
        val imageUriString = sharedPreferences.getString("legendImageUri", null)

        // Mostrar los datos existentes en los campos del formulario
        binding.etNombre.setText(nombre)
        binding.etApodo.setText(apodo)
        binding.etDescripcion.setText(descripcion)
        binding.etFechaNacimiento.setText(fechaNacimiento)
        binding.etOcupacion.setText(ocupacion)
        binding.etPRBenchPress.setText(prBenchPress)
        binding.etPRSquat.setText(prSquat)
        binding.etPRDeadlift.setText(prDeadlift)

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            selectedImageUri = imageUri
            Glide.with(this).load(imageUri).into(binding.imagePreview)
        }

        // Configurar botón "Seleccionar Foto"
        binding.btnSeleccionarFoto.setOnClickListener {
            openGallery()
        }

        // Configurar botón "Guardar"
        binding.btnGuardar.setOnClickListener {
            val nombreNuevo = binding.etNombre.text.toString()
            val apodoNuevo = binding.etApodo.text.toString()
            val descripcionNueva = binding.etDescripcion.text.toString()
            val fechaNacimientoNueva = binding.etFechaNacimiento.text.toString()
            val ocupacionNueva = binding.etOcupacion.text.toString()
            val prBenchPressNuevo = binding.etPRBenchPress.text.toString()
            val prSquatNuevo = binding.etPRSquat.text.toString()
            val prDeadliftNuevo = binding.etPRDeadlift.text.toString()

            // Validar que los campos no estén vacíos
            if (nombreNuevo.isNotEmpty() && apodoNuevo.isNotEmpty() && descripcionNueva.isNotEmpty() &&
                fechaNacimientoNueva.isNotEmpty() && ocupacionNueva.isNotEmpty() &&
                prBenchPressNuevo.isNotEmpty() && prSquatNuevo.isNotEmpty() && prDeadliftNuevo.isNotEmpty()) {

                if (selectedImageUri == null) {
                    Toast.makeText(this, "Por favor selecciona una foto", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Guardar en SharedPreferences
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

                // Continuar con la navegación
                val intent = Intent(this, UserLegendActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            if (selectedImageUri != null) {
                // Mostrar la imagen seleccionada en un ImageView
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(binding.imagePreview)
                Toast.makeText(this, "Foto seleccionada con éxito", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No se seleccionó ninguna foto", Toast.LENGTH_SHORT).show()
        }
    }
}