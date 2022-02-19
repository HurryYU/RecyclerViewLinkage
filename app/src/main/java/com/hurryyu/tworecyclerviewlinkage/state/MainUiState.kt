package com.hurryyu.tworecyclerviewlinkage.state

import com.hurryyu.tworecyclerviewlinkage.bean.ProductsDataViewBean

data class MainUiState(
    val currentSelectedIndex:Int = 0,
    val isLoading: Boolean = true,
    val productsData: ProductsDataViewBean = ProductsDataViewBean()
)
