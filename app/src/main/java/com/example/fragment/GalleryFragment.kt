package com.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.databinding.FragmentGalleryBinding
import com.example.adapter.GalleryAdapter
import com.example.viewmodel.HomeViewModel

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        observeViewModel()

        viewModel.loadHomeData()
    }

    private fun setupRecyclerView() {
        galleryAdapter = GalleryAdapter(listOf())
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvGallery.adapter = galleryAdapter
    }

    private fun observeViewModel() {
        viewModel.galleryItems.observe(viewLifecycleOwner) { items ->
            galleryAdapter.updateData(items)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
