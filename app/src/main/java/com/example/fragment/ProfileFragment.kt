package com.example.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.EditProfileActivity
import com.example.LoginActivity
import com.example.MainActivity
import com.example.ManageAddressesActivity
import com.example.SavedDesignsActivity
import com.example.databinding.FragmentProfileBinding
import com.example.utils.SessionManager
import com.example.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        setupClickListeners()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUserProfile(sessionManager.getUserId())
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        binding.btnManageAddresses.setOnClickListener {
            startActivity(Intent(requireActivity(), ManageAddressesActivity::class.java))
        }

        binding.btnSavedDesigns.setOnClickListener {
            startActivity(Intent(requireActivity(), SavedDesignsActivity::class.java))
        }

        // Knowledge Center replaces fragment inside MainActivity
        binding.btnGallery.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(GalleryFragment(), true)
        }

        binding.btnGuidelines.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(GuidelinesFragment(), true)
        }

        binding.btnFaqs.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(FAQFragment(), true)
        }

        binding.btnAboutMe.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(com.example.fragment.AboutMeFragment(), true)
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun observeViewModel() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.txtProfileName.text = it.name
                binding.txtProfileEmail.text = it.email
                binding.txtProfilePhone.text = it.phone
                binding.txtAvatarLetter.text = if (it.name.isNotEmpty()) it.name[0].uppercase() else "P"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
