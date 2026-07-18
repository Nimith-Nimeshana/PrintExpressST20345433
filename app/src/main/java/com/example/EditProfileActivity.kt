package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityEditProfileBinding
import com.example.utils.SessionManager
import com.example.viewmodel.ProfileViewModel

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        sessionManager = SessionManager(this)

        // Bind current fields
        binding.etName.setText(sessionManager.getUserName())
        binding.etPhone.setText(sessionManager.getUserPhone())
        binding.etAddress.setText(sessionManager.getUserAddress())

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()

            viewModel.updateProfile(sessionManager.getUserId(), name, phone, address)
        }
    }

    private fun observeViewModel() {
        viewModel.profileUpdateResult.observe(this) { result ->
            result.onSuccess {
                val name = binding.etName.text.toString().trim()
                val phone = binding.etPhone.text.toString().trim()
                val address = binding.etAddress.text.toString().trim()

                // Update session state
                sessionManager.updateProfileSession(name, phone, address)

                Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message ?: "Update Failed!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
