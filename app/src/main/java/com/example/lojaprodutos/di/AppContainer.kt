package com.example.lojaprodutos.di

import android.content.Context
import com.example.lojaprodutos.data.repository.CouponRepositoryImpl
import com.example.lojaprodutos.data.repository.ProductRepositoryImpl
import com.example.lojaprodutos.data.source.local.MyDatabase
import com.example.lojaprodutos.domain.service.DiscountService
import com.example.lojaprodutos.domain.usecase.AddCouponUseCase
import com.example.lojaprodutos.domain.usecase.AddProductUseCase
import com.example.lojaprodutos.domain.usecase.BuyProductsUseCase
import com.example.lojaprodutos.domain.usecase.GetAllProductsUseCase

class AppContainer(context: Context) {
    // Local Database
    private val database = MyDatabase.getDatabase(context)

    // DAOs
    private val productDao = database.getProductDao()
    private val couponDao = database.getCouponDao()

    // Repositories
    val productRepository by lazy {
        ProductRepositoryImpl(productDao)
    }

    val couponRepository by lazy {
        CouponRepositoryImpl(couponDao)
    }

    // Services
    val discountService by lazy {
        DiscountService(couponRepository)
    }

    // Use Cases
    val getAllProductsUseCase by lazy {
        GetAllProductsUseCase(productRepository)
    }

    val addProductUseCase by lazy {
        AddProductUseCase(productRepository)
    }

    val addCouponUseCase by lazy {
        AddCouponUseCase(couponRepository)
    }

    val buyProductsUseCase by lazy {
        BuyProductsUseCase(productRepository, discountService)
    }
}