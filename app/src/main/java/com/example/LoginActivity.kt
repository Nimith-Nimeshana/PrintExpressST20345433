package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityLoginBinding
import com.example.utils.SessionManager
import com.example.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Login Action button click listener
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val rememberMe = binding.cbRememberMe.isChecked

            viewModel.login(email, password)
        }

        // SignUp click
        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Observing ViewModel Auth Result
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { user ->
                val rememberMe = binding.cbRememberMe.isChecked
                sessionManager.createSession(
                    userId = user.userId,
                    name = user.name,
                    email = user.email,
                    phone = user.phone,
                    address = user.address,
                    rememberMe = rememberMe
                )
                Toast.makeText(this, "Welcome, ${user.name}!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message ?: "Authentication Failed.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
