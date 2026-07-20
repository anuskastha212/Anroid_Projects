package com.example.esewa_project.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.esewa_project.R
import com.example.esewa_project.data.api.RetrofitInstance
import com.example.esewa_project.databinding.FragmentHomeBinding
import com.example.esewa_project.ui.adapter.ProductAdapter
import kotlinx.coroutines.launch
import kotlin.collections.take


class HomeFragment : Fragment(R.layout.fragment_home){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFeaturedProductRecyclerView()
        getProducts()
    }

    private fun setupFeaturedProductRecyclerView() = binding.rvProducts.apply {

        productAdapter = ProductAdapter { product ->
            Toast.makeText(
                requireContext(),
                product.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        adapter = productAdapter

        layoutManager = GridLayoutManager(    requireContext(),2)
    }

    private fun getProducts() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val response = RetrofitInstance.apiInterface.getData()

                if (response.isSuccessful) {

                    val apiResponse = response.body()
                    val actualProducts = apiResponse?.data

                    if (actualProducts != null) {
                        productAdapter.products = actualProducts.take(4)
                    }

                } else {
                    Log.e("HomeFragment", "API Error: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("HomeFragment", "Exception: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}