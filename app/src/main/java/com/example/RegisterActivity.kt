package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityRegisterBinding
import com.example.viewmodel.LoginViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            viewModel.register(name, email, phone, password, confirmPassword)
        }

        binding.txtSignIn.setOnClickListener {
            finish()
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { success ->
                if (success) {
                    Toast.makeText(this, "Account Created successfully!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }.onFailure { exception ->
                Toast.makeText(this, exception.message ?: "Registration Failed!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
