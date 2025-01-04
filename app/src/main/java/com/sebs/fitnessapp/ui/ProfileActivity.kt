package com.sebs.fitnessapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sebs.fitnessapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("LegendPrefs", MODE_PRIVATE)

        // Prellenar el formulario si ya existen datos guardados
        if (sharedPreferences.getBoolean("isLegendCreated", false)) {
            binding.etNombre.setText(sharedPreferences.getString("legendName", ""))
            binding.etApodo.setText(sharedPreferences.getString("legendAlias", ""))
            binding.etDescripcion.setText(sharedPreferences.getString("legendDescription", ""))
            binding.etFechaNacimiento.setText(sharedPreferences.getString("legendBirthdate", ""))
            binding.etOcupacion.setText(sharedPreferences.getString("legendOccupation", ""))
            binding.etPRBenchPress.setText(sharedPreferences.getString("legendBenchPress", ""))
            binding.etPRSquat.setText(sharedPreferences.getString("legendSquat", ""))
            binding.etPRDeadlift.setText(sharedPreferences.getString("legendDeadlift", ""))
        }

        // Configurar botón "Guardar"
        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val apodo = binding.etApodo.text.toString()
            val descripcion = binding.etDescripcion.text.toString()
            val fechaNacimiento = binding.etFechaNacimiento.text.toString()
            val ocupacion = binding.etOcupacion.text.toString()
            val prBenchPress = binding.etPRBenchPress.text.toString()
            val prSquat = binding.etPRSquat.text.toString()
            val prDeadlift = binding.etPRDeadlift.text.toString()

            // Validar que los campos no estén vacíos
            if (nombre.isNotEmpty() && apodo.isNotEmpty() && descripcion.isNotEmpty() &&
                fechaNacimiento.isNotEmpty() && ocupacion.isNotEmpty() &&
                prBenchPress.isNotEmpty() && prSquat.isNotEmpty() && prDeadlift.isNotEmpty()) {

                // Guardar en SharedPreferences
                sharedPreferences.edit()
                    .putBoolean("isLegendCreated", true)
                    .putString("legendName", nombre)
                    .putString("legendAlias", apodo)
                    .putString("legendDescription", descripcion)
                    .putString("legendBirthdate", fechaNacimiento)
                    .putString("legendOccupation", ocupacion)
                    .putString("legendBenchPress", prBenchPress)
                    .putString("legendSquat", prSquat)
                    .putString("legendDeadlift", prDeadlift)
                    .apply()

                // Mostrar mensaje de éxito
                Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()

                // Redirigir a UserLegendActivity
                val intent = Intent(this, UserLegendActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}