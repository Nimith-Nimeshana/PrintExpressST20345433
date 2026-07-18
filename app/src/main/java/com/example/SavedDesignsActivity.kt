package com.example

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding.ActivitySavedDesignsBinding
import com.example.utils.SessionManager
import com.example.adapter.SavedDesignAdapter
import com.example.viewmodel.ProfileViewModel

class SavedDesignsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedDesignsBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: SavedDesignAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedDesignsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        sessionManager = SessionManager(this)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadSavedDesigns(sessionManager.getUserId())
    }

    private fun setupRecyclerView() {
        adapter = SavedDesignAdapter(listOf()) { design ->
            viewModel.deleteSavedDesign(sessionManager.getUserId(), design.designId)
        }
        binding.rvSavedDesigns.layoutManager = LinearLayoutManager(this)
        binding.rvSavedDesigns.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.savedDesigns.observe(this) { designs ->
            if (designs.isEmpty()) {
                binding.rvSavedDesigns.visibility = View.GONE
                binding.layoutEmptyDesigns.visibility = View.VISIBLE
            } else {
                binding.rvSavedDesigns.visibility = View.VISIBLE
                binding.layoutEmptyDesigns.visibility = View.GONE
                adapter.updateData(designs)
            }
        }
    }
}
