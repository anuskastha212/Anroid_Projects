package com.example.esewa_project.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.esewa_project.BannerIndicator
import com.example.esewa_project.R
import com.example.esewa_project.data.api.RetrofitInstance
import com.example.esewa_project.data.source.BannerImages
import com.example.esewa_project.data.source.CategoryData
import com.example.esewa_project.databinding.FragmentHomeBinding
import com.example.esewa_project.ui.adapter.BannerAdapter
import com.example.esewa_project.ui.adapter.CategoryAdapter
import com.example.esewa_project.ui.adapter.MostPopularAdapter
import com.example.esewa_project.ui.adapter.FeaturedProductAdapter
import com.example.esewa_project.ui.adapter.HotDealsAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch
import kotlin.collections.take


class HomeFragment : Fragment(R.layout.fragment_home){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var layoutDots: LinearLayout
    private lateinit var dots: Array<ImageView?>

    private val bannerIndicator by lazy { BannerIndicator() }
    private val categoryData by lazy { CategoryData() }
    private val bannerImages by lazy { BannerImages() }

    private lateinit var featuredProductAdapter: FeaturedProductAdapter
    private lateinit var mostPopularAdapter: MostPopularAdapter
    private lateinit var hotDealsAdapter: HotDealsAdapter

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

        setupBanner()
        setupCategories()
        setupFeaturedProductRecyclerView()
        getFeaturedProducts()
        setupHotDealsRecyclerView()
        getHotDealsProducts()
        setupMostPopularRecyclerView()
        fetchMostPopularCategories()
    }
    private fun setupBanner() {
        viewPager = binding.viewPagerBanner
        layoutDots = binding.layoutDots

        val imagesList = bannerImages.getBannerImages()
        viewPager.adapter = BannerAdapter(imagesList)

        dots = bannerIndicator.setupIndicator(requireContext(), imagesList.size, layoutDots)
        bannerIndicator.setCurrentIndicator(requireContext(), 0, layoutDots)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bannerIndicator.setCurrentIndicator(requireContext(), position, layoutDots)
            }
        })
    }

    private fun setupCategories() {
        binding.rvCategories.adapter = CategoryAdapter(categoryData.getCategoryData())
    }
    private fun setupFeaturedProductRecyclerView() = binding.rvFeaturedProducts.apply {

        featuredProductAdapter = FeaturedProductAdapter { product ->
            Toast.makeText(
                requireContext(),
                product.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        adapter = featuredProductAdapter

        layoutManager = GridLayoutManager(    requireContext(),2)
    }

    private fun getFeaturedProducts() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val response = RetrofitInstance.productApi.getFeaturedData()

                if (response.isSuccessful) {

                    val apiResponse = response.body()
                    val actualProducts = apiResponse?.data

                    if (actualProducts != null) {
                        featuredProductAdapter.products = actualProducts.take(2)
                    }

                } else {
                    Log.e("HomeFragment", "API Error: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("HomeFragment", "Exception: ${e.message}")
            }
        }
    }

    private fun setupHotDealsRecyclerView() = binding.rvHotDeals.apply {

        hotDealsAdapter = HotDealsAdapter() { product ->
            Toast.makeText(
                requireContext(),
                product.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        adapter = hotDealsAdapter

        layoutManager = GridLayoutManager(    requireContext(),2)
    }


    private fun getHotDealsProducts() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val response = RetrofitInstance.productApi.getHotDealsData()

                if (response.isSuccessful) {

                    val apiResponse = response.body()
                    val actualProducts = apiResponse?.data

                    if (actualProducts != null) {
                        hotDealsAdapter.products = actualProducts.drop(2).take(2)
                    }

                } else {
                    Log.e("HomeFragment", "API Error: ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("HomeFragment", "Exception: ${e.message}")
            }
        }
    }


    private fun setupMostPopularRecyclerView(){
        val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
            mostPopularAdapter = MostPopularAdapter(emptyList()){ categoryData ->
                Toast.makeText(requireContext(),
                    "Clicked: $categoryData",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            binding.rvMostPopular.apply{
                layoutManager = flexboxLayoutManager
                adapter= mostPopularAdapter
                isNestedScrollingEnabled = false
            }
    }
    private fun fetchMostPopularCategories() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = RetrofitInstance.api.getMostPopular()

                if (response.isSuccessful) {
                    response.body()?.let {
                        mostPopularAdapter.updateData(it)
                    }
                }
            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}