package com.example.esewa_project.ui.fragments

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.esewa_project.BannerIndicator
import com.example.esewa_project.ProductDetailActivity
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
import com.example.esewa_project.ui.adapter.PopularBrandAdapter
import com.example.esewa_project.ui.adapter.RecommendedAdapter
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
    private lateinit var popularBrandAdapter: PopularBrandAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter

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
        setupHotDealsRecyclerView()
        getAllProducts()
        setupPopularBrandRecyclerView()
        setupRecommendedRecyclerView()
        setupMostPopularRecyclerView()
        getMostPopularCategories()
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
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        adapter = featuredProductAdapter

        layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false)
        isNestedScrollingEnabled = false
    }

    private fun setupHotDealsRecyclerView() = binding.rvHotDeals.apply {

        hotDealsAdapter = HotDealsAdapter() { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        adapter = hotDealsAdapter

        layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false)
        isNestedScrollingEnabled = false
    }

    private fun setupPopularBrandRecyclerView() = binding.rvPopularBrands.apply {

        popularBrandAdapter = PopularBrandAdapter() { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        adapter = popularBrandAdapter

        layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false)
        isNestedScrollingEnabled = false
    }

    private fun setupRecommendedRecyclerView() = binding.rvRecommended.apply {

        recommendedAdapter = RecommendedAdapter() { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        adapter = recommendedAdapter

        layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false)
        isNestedScrollingEnabled = false
    }

    private fun getAllProducts() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val products = RetrofitInstance.productApi.getAllProducts()

                Log.d("API", "Products = ${products.size}")
                Log.d("API", products.toString())

                featuredProductAdapter.products = products.take(7)
                hotDealsAdapter.products = products.drop(7).take(7)
                popularBrandAdapter.products = products.drop(14).take(7)
                recommendedAdapter.products = products.drop(21).take(7)

            } catch (e: Exception) {
                Log.e("API", "Exception", e)
            }
        }
    }

    private fun getProduct(id: Int) {

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val product = RetrofitInstance.productApi.getProductById(id)

                Log.d("PRODUCT", product.toString())

            } catch (e: Exception) {
                Log.e("PRODUCT", "Error", e)
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
                    " $categoryData",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            binding.rvMostPopular.apply{
                layoutManager = flexboxLayoutManager
                adapter= mostPopularAdapter
                isNestedScrollingEnabled = false
            }
    }
    private fun getMostPopularCategories() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = RetrofitInstance.api.getMostPopular()

                if (response.isSuccessful) {
                    response.body()?.let {categories ->
                        mostPopularAdapter.updateData(categories.take((7)))
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