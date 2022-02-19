package com.hurryyu.tworecyclerviewlinkage.bean

data class ProductsApiBean(
    val `data`: List<Classify> = listOf()
)

data class Classify(
    val productClassify: String = "",
    val productImgUrl: String = "",
    val productInfo: List<ProductInfo> = listOf()
)

data class ProductInfo(
    val describe: String = "",
    val imgUrl: String = "",
    val price: String = "",
    val title: String = ""
)