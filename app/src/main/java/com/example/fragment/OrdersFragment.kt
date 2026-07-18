package com.example.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.OrderDetailsActivity
import com.example.databinding.FragmentOrdersBinding
import com.example.utils.SessionManager
import com.example.adapter.OrderAdapter
import com.example.viewmodel.OrdersViewModel

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: OrdersViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUserOrders(sessionManager.getUserId())
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(listOf()) { order ->
            val intent = Intent(requireActivity(), OrderDetailsActivity::class.java).apply {
                putExtra("ORDER_ID", order.orderId)
            }
            startActivity(intent)
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = orderAdapter
    }

    private fun observeViewModel() {
        viewModel.orderList.observe(viewLifecycleOwner) { orders ->
            if (orders.isEmpty()) {
                binding.rvOrders.visibility = View.GONE
                binding.layoutEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvOrders.visibility = View.VISIBLE
                binding.layoutEmptyState.visibility = View.GONE
                orderAdapter.updateData(orders)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
