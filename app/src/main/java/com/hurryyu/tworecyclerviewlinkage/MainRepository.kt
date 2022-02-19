package com.hurryyu.tworecyclerviewlinkage

import com.google.gson.Gson
import com.hurryyu.tworecyclerviewlinkage.bean.ClassifyViewBean
import com.hurryyu.tworecyclerviewlinkage.bean.ProductInfoViewBean
import com.hurryyu.tworecyclerviewlinkage.bean.ProductsApiBean
import com.hurryyu.tworecyclerviewlinkage.bean.ProductsDataViewBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MainRepository {
    private var cacheProductsDataBean: ProductsDataViewBean? = null

    suspend fun loadProducts(): ProductsDataViewBean = withContext(Dispatchers.IO) {
        val reader = App.context.assets.open("products.json").reader()
        val classifyList = Gson().fromJson(reader.readText(), ProductsApiBean::class.java).data
        val classifyViewList = classifyList.map {
            ClassifyViewBean(
                it.productClassify,
                it.productImgUrl
            )
        }
        val productInfoList = mutableListOf<ProductInfoViewBean>()
        for (classify in classifyList) {
            for ((i, productInfo) in classify.productInfo.withIndex()) {
                productInfoList.add(
                    ProductInfoViewBean(
                        productInfo.describe,
                        productInfo.imgUrl,
                        productInfo.price,
                        productInfo.title,
                        classify.productClassify,
                        i == 0
                    )
                )
            }
        }
        ProductsDataViewBean(classifyViewList, productInfoList).also { cacheProductsDataBean = it }
    }

    suspend fun findFirstGroupPositionByGroupName(groupName: String): Int =
        withContext(Dispatchers.IO) {
            cacheProductsDataBean?.productInfoList?.let {
                it.indexOfFirst { productInfo -> productInfo.groupName == groupName }
            } ?: -1
        }

    suspend fun findClassifyIndexByGroupName(groupName: String): Int = withContext(Dispatchers.IO) {
        cacheProductsDataBean?.classifyViewList?.let {
            it.indexOfFirst { classifyViewBean -> classifyViewBean.productClassify == groupName }
        } ?: -1
    }
}