package com.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding.ActivityManageAddressesBinding
import com.example.databinding.DialogAddAddressBinding
import com.example.utils.SessionManager
import com.example.adapter.AddressAdapter
import com.example.viewmodel.ProfileViewModel

class ManageAddressesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageAddressesBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageAddressesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        sessionManager = SessionManager(this)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        viewModel.loadAddresses(sessionManager.getUserId())
    }

    private fun setupRecyclerView() {
        adapter = AddressAdapter(listOf()) { address ->
            viewModel.deleteAddress(sessionManager.getUserId(), address.addressId)
        }
        binding.rvAddresses.layoutManager = LinearLayoutManager(this)
        binding.rvAddresses.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnAddAddress.setOnClickListener {
            showAddAddressDialog()
        }
    }

    private fun showAddAddressDialog() {
        val dialogBinding = DialogAddAddressBinding.inflate(LayoutInflater.from(this))

        AlertDialog.Builder(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog_Alert)
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { dialog, _ ->
                val fullAddress = dialogBinding.etDialogAddress.text.toString().trim()
                val city = dialogBinding.etDialogCity.text.toString().trim()
                val postalCode = dialogBinding.etDialogPostalCode.text.toString().trim()

                viewModel.addAddress(sessionManager.getUserId(), fullAddress, city, postalCode)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun observeViewModel() {
        viewModel.addressList.observe(this) { addresses ->
            if (addresses.isEmpty()) {
                binding.rvAddresses.visibility = View.GONE
                binding.layoutEmptyAddresses.visibility = View.VISIBLE
            } else {
                binding.rvAddresses.visibility = View.VISIBLE
                binding.layoutEmptyAddresses.visibility = View.GONE
                adapter.updateData(addresses)
            }
        }

        viewModel.addressOperationResult.observe(this) { result ->
            result.onFailure { exception ->
                Toast.makeText(this, exception.message ?: "Operation Failed!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
