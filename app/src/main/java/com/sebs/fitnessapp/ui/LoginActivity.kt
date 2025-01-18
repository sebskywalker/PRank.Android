package com.sebs.fitnessapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.sebs.fitnessapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var contrasenia = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            actionLoginSuccessful()
        }

        binding.btnLogin.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            authenticateUser(email, contrasenia)
        }

        binding.btnRegistrarse.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            registerUser(email, contrasenia)
        }

        binding.tvRestablecerPassword.setOnClickListener {
            showPasswordResetDialog()
        }
    }

    private fun validateFields(): Boolean {
        email = binding.tietEmail.text.toString().trim()
        contrasenia = binding.tietContrasenia.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tietEmail.error = "Correo inválido"
            binding.tietEmail.requestFocus()
            return false
        }
        if (contrasenia.isEmpty() || contrasenia.length < 6) {
            binding.tietContrasenia.error = "Contraseña inválida (mínimo 6 caracteres)"
            binding.tietContrasenia.requestFocus()
            return false
        }
        return true
    }

    private fun authenticateUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                actionLoginSuccessful()
            } else {
                handleErrors(task.exception as FirebaseAuthException)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseAuth.currentUser?.sendEmailVerification()
                Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                actionLoginSuccessful()
            } else {
                handleErrors(task.exception as FirebaseAuthException)
            }
        }
    }

    private fun handleErrors(exception: FirebaseAuthException) {
        when (exception.errorCode) {
            "ERROR_INVALID_EMAIL" -> binding.tietEmail.error = "Formato de correo inválido"
            "ERROR_WEAK_PASSWORD" -> binding.tietContrasenia.error = "Contraseña débil"
            "ERROR_USER_NOT_FOUND" -> Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPasswordResetDialog() {
        val resetMail = EditText(this).apply {
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        AlertDialog.Builder(this)
            .setTitle("Restablecer contraseña")
            .setMessage("Ingrese su correo electrónico")
            .setView(resetMail)
            .setPositiveButton("Enviar") { _, _ ->
                val mail = resetMail.text.toString()
                if (mail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    firebaseAuth.sendPasswordResetEmail(mail)
                        .addOnSuccessListener { Toast.makeText(this, "Correo enviado", Toast.LENGTH_SHORT).show() }
                        .addOnFailureListener { Toast.makeText(this, "Error al enviar el correo", Toast.LENGTH_SHORT).show() }
                } else {
                    Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun actionLoginSuccessful() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        val sharedPreferences = getSharedPreferences("LegendPrefs_$uid", MODE_PRIVATE)

        if (sharedPreferences.getString("legendName", null) == null) {
            sharedPreferences.edit()
                .putString("legendName", "Nuevo Usuario")
                .putString("legendAlias", "Alias")
                .putString("legendDescription", "Descripción pendiente")
                .putString("legendBirthdate", "01/01/2000")
                .putString("legendOccupation", "Ocupación pendiente")
                .putString("legendBenchPress", "0")
                .putString("legendSquat", "0")
                .putString("legendDeadlift", "0")
                .apply()
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}