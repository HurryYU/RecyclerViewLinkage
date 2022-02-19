package com.hurryyu.tworecyclerviewlinkage.bean

data class ProductsDataViewBean(
    val classifyViewList: List<ClassifyViewBean> = mutableListOf(),
    val productInfoList: List<ProductInfoViewBean> = mutableListOf()
)
