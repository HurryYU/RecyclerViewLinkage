package com.hurryyu.tworecyclerviewlinkage.bean

import java.util.*

data class ProductInfoViewBean(
    val describe: String = "",
    val imgUrl: String = "",
    val price: String = "",
    val title: String = "",
    val groupName: String = "",
    val isGroupFirst: Boolean = false,
    val id:String = UUID.randomUUID().toString()
)
