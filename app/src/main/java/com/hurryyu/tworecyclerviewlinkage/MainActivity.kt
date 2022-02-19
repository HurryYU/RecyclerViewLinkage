package com.hurryyu.tworecyclerviewlinkage

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_END
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import androidx.recyclerview.widget.RecyclerView
import com.hurryyu.tworecyclerviewlinkage.adapter.ClassifyAdapter
import com.hurryyu.tworecyclerviewlinkage.adapter.ProductInfoAdapter
import com.hurryyu.tworecyclerviewlinkage.databinding.ActivityMainBinding
import com.hurryyu.tworecyclerviewlinkage.util.smoothScrollToPosition
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var classifyAdapter: ClassifyAdapter
    private val productInfoAdapter = ProductInfoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listenerData()
        listenerRecyclerView()
        classifyAdapter = ClassifyAdapter() { position ->
            classifyAdapter.isPrimaryClicked = true
            vm.changeClassifySelectedIndex(position)
            vm.adjustProductListPosition(classifyAdapter.dataList[position].productClassify)
        }
        binding.rvClassify.adapter = classifyAdapter
        binding.rvProduct.apply {
            adapter = productInfoAdapter
            addItemDecoration(ProductDecoration())
        }
    }

    private fun listenerRecyclerView() {
        var lastGroupName = ""
        binding.rvProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    classifyAdapter.isPrimaryClicked = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (classifyAdapter.isPrimaryClicked) {
                    return
                }
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
                val groupName = productInfoAdapter.getGroupName(visiblePosition)
                if (groupName != lastGroupName) {
                    lastGroupName = groupName
                    vm.changeClassifySelectedIndexByGroupName(groupName)
                }
            }
        })
    }

    private fun listenerData() {
        lifecycleScope.launchWhenStarted {
            vm.mainStateFlow.map { it.isLoading }.distinctUntilChanged().collect {
                binding.pb.visibility = if (it) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            vm.mainStateFlow.map { it.productsData }.distinctUntilChanged().collect {
                classifyAdapter.setDataList(it.classifyViewList)
                productInfoAdapter.submitList(it.productInfoList)
            }
        }

        lifecycleScope.launchWhenStarted {
            vm.mainStateFlow.map { it.currentSelectedIndex }.distinctUntilChanged()
                .collect {
                    classifyAdapter.currentSelectedPosition = it
                    smoothScrollToPosition(binding.rvClassify, SNAP_TO_END, it)
                }
        }

        lifecycleScope.launchWhenStarted {
            vm.productListScrollPositionFlow.collect {
                smoothScrollToPosition(binding.rvProduct, SNAP_TO_START, it)
            }
        }
    }
}