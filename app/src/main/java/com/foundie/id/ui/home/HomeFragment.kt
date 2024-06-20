package com.foundie.id.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.R
import com.foundie.id.data.adapter.CatalogAdapter
import com.foundie.id.data.adapter.GroupCommunityAdapter
import com.foundie.id.data.adapter.ImageSliderAdapter
import com.foundie.id.data.local.response.GroupsDataItem
import com.foundie.id.data.local.response.ImageDataResponse
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.databinding.FragmentHomeBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.delayTimeSlider
import com.foundie.id.ui.catalog.CatalogViewModel
import com.foundie.id.ui.community.CommunityViewModel
import com.foundie.id.ui.home.color_analysis.input.ColorAnalysisInputFirstFragment
import com.foundie.id.ui.home.compare_product.CompareProductInputFragment
import com.foundie.id.ui.home.makeup_analysis.MakeupAnalysisInputFragment
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.CatalogViewModelFactory
import com.foundie.id.viewmodel.CommunityViewModelFactory

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ImageSliderAdapter
    private val list = ArrayList<ImageDataResponse>()
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private val handler = Handler()
    private var currentPage = 0
    private lateinit var runnable: Runnable
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var groupAdapter: GroupCommunityAdapter
    private val catalogviewModel: CatalogViewModel by lazy {
        ViewModelProvider(
            this,
            CatalogViewModelFactory(requireContext())
        )[CatalogViewModel::class.java]
    }
    private val groupviewModel: CommunityViewModel by lazy {
        ViewModelProvider(
            this,
            CommunityViewModelFactory(requireContext())
        )[CommunityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.hi_gorgeous)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        setupRecyclerView()
        setupViewPager()
        observeViewModel()
        btnClick()
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            catalogviewModel.getProduct(token)
            groupviewModel.getListGroup(token)
        }
    }

    private fun setupRecyclerView() {
        binding.rvListCatalog.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            catalogAdapter = CatalogAdapter()
            adapter = catalogAdapter
        }
        binding.rvListCommunity.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            groupAdapter = GroupCommunityAdapter()
            adapter = groupAdapter
        }
    }

    private fun setupViewPager() {
        list.add(ImageDataResponse("https://storage.googleapis.com/storage-foundie/data/images/slider-home/1.png"))
        list.add(ImageDataResponse("https://storage.googleapis.com/storage-foundie/data/images/slider-home/2.png"))

        adapter = ImageSliderAdapter(list)
        binding.viewPager.adapter = adapter

        runnable = Runnable {
            if (currentPage == list.size) currentPage = 0
            binding.viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(runnable, delayTimeSlider)
        }

        handler.postDelayed(runnable, delayTimeSlider)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                super.onPageSelected(position)
            }
        })
    }

    private fun observeViewModel() {
        catalogviewModel.isLoadingProduct.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        groupviewModel.isLoadingListGroup.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        catalogviewModel.product.observe(viewLifecycleOwner) { productList ->
            setProductData(productList)
        }

        groupviewModel.listGroup.observe(viewLifecycleOwner) { productList ->
            setGroupData(productList)
        }

        catalogviewModel.productStatus.observe(viewLifecycleOwner) { productStatus ->
            val isError = catalogviewModel.isErrorProduct
            if (isError && !productStatus.isNullOrEmpty()) {
                binding.rvListCatalog.visibility = View.VISIBLE
            }
        }

        groupviewModel.listGroupStatus.observe(viewLifecycleOwner) { groupStatus ->
            val isError = groupviewModel.isErrorListGroup
            if (isError && !groupStatus.isNullOrEmpty()) {
                binding.rvListCommunity.visibility = View.VISIBLE
            }
        }
    }

    private fun btnClick() {
        binding.apply {
            imgMakeupAnalysis.setOnClickListener {
                replaceFragment(MakeupAnalysisInputFragment())
            }
            imgColorAnalysis.setOnClickListener {
                replaceFragment(ColorAnalysisInputFirstFragment())
            }
            imgCompareProduct.setOnClickListener {
                replaceFragment(CompareProductInputFragment())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setProductData(productList: List<ProductData>) {
        if (::catalogAdapter.isInitialized) {
            if (productList.isNotEmpty()) {
                catalogAdapter.setData(productList)
            }
        }
    }

    private fun setGroupData(groupList: List<GroupsDataItem>) {
        if (::groupAdapter.isInitialized) {
            if (groupList.isNotEmpty()) {
                groupAdapter.setData(groupList)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
